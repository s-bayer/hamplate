object TagInterpreter extends Interpreter {
  case class Tag(var tagname: String, depth: Int, var content: String) {
    // Used to sort the attributes
    import scala.collection.immutable.TreeMap
    private var attributes: TreeMap[String,String] = TreeMap()
    private val prepend = "  "*depth

    def toHtml: String = {
      var result = ""
      var starttag = tagname

      if(!attributes.isEmpty) {
        starttag+=" "+attributes.map{case(a,b)=>a+"='"+b+"'"}.mkString(" ")
      }
      
      // Multiline content
      if(content.trim.contains("\n")) {
        result += prepend+"<"+starttag+">\n"
        result += prepend+"  "+content.trim+"\n"
        result += prepend+"</"+tagname+">\n"
      // Single Line content
      } else if(content.trim != "") {
        result += prepend+"<"+starttag+"> "
        result += content.trim
        result += " </"+tagname+">\n"
      // Empty tag
      } else {
        result += prepend+"<"+starttag+"/>\n"
      }
      result
    }

    /**
      * Add an attribute.
      * If it is already set, add the content to the list.
      * Example:
      *   before: class="class1 class2"
      *     name="class" content="class3 class4"
      *   after:  class="class1 class2 class3 class4"
      */
    def addAttribute(name: String, content: String) {
      if(content.trim != "") {
        if(attributes.isDefinedAt(name)){
          attributes += name -> (attributes(name)+" "+content)
        } else {
          attributes += name -> content
        }
      }
    }

    /**
      * Set an attribute. 
      * If it is already set, throw an exception.
      **/
    // TODO throw the exception
    def setAttribute(name: String, content: String) {
      if(attributes.isDefinedAt(name)) {
        throw new Exception("Duplicate key '"+content+"' for attribute "+name+"='"+attributes(name)+"'")
      }
      if(content.trim != "") {
        attributes += name -> content
      }
    }

    /**
      * Add a set of attributes
      */
    def addAttributes(a: String) {
      val AttributeRegex = attributeRegex.r
      val list = AttributeRegex.findAllIn(a).toList.filter(_.trim != "")
      // Split key='value' into (key,'value')
      val pairs = list.map(_.span(_ != '=')) // split attribute in "key" and "='value'"
        .map{case(a,b)=> (a,b.tail.trim)} // remove = before attributes value
        .map{case(a,b)=> (a,b.tail.init)} // remove "" and '' around attributes value
      for(x <- pairs.toMap) {
        addAttribute(x._1,x._2)
      }
    }
  }
  object Tag{
    def buildFrom(line: Line) : Tag = {
      Tag(tagname=linesign(line),
        line.depth,
        content="")
    }
  }

  override def sign = ""
  override def toHtml(line: Line):String = {
    if(line.hasChildren) {
      renderWithChildren(line).toHtml
    } else {
      renderWithoutChildren(line).toHtml
    }
  }

  val attributeRegex = {
    // match any valid attribute key
    val keyRegex = """([\w\-]+)"""
    // match any valid string (only double quoted)
    val valueRegex = """(["']([^"]|\\")*["'])"""
    "("+keyRegex+"="+valueRegex+"""\s*)"""
  }

  val MatchRegex = {
    // find all attributes in the first group and the rest of the text in the second
    ("("+attributeRegex+"*)"+"(.*)").r//"""(.*)""".r//
  }

  def parseTail(value: String):(String,String) = {
    value match {
      case MatchRegex(a,b,c,d,e,f) => {(a,f)}
    }
  }

  // Used to convert "tagname.class1#bla.class2" to <tagname class="class1 class2" id="bla">
  def parseTag(tag: Tag, line: Line):Tag = {
    tag.tagname = tag.tagname.trim

    // Replace .class1 with class="class1"
    val matchClass = """\.([^\.#]*)"""
    val classSeq = matchClass.r.findAllIn(tag.tagname)
    val classAttr = classSeq.mkString(" ").replace(".","")
    tag.tagname = tag.tagname.replaceAll(matchClass,"")
    tag.addAttribute("class",classAttr)

    // Replace #id1 with id="id1"
    val matchId = """\#([^\.#]*)"""
    val idSeq = matchId.r.findAllIn(tag.tagname)
    for(idAttr <- idSeq.toList) {
      tag.setAttribute("id",idAttr.replace("#",""))
    }
    tag.tagname = tag.tagname.replaceAll(matchId,"")

    tag
  }

  def createText(tag: Tag, line: Line):Tag = {
    tailofline(line) match {
      case Some(tail) => {
        val (attributes, text) = parseTail(tail.trim)
        tag.addAttributes(attributes)
        if(text.trim != "") {
          tag.content = text+"\n"
        }
      }
      case _ => ()
    }
    tag
  }

  def processTag(tag: Tag, line: Line):Tag = {
    parseTag(createText(tag,line),line)
  }

  def renderWithoutChildren(line:Line):Tag = {
    var result=Tag.buildFrom(line)

    result = processTag(result, line)

    result
  }

  def renderWithChildren(line: Line):Tag = {
    // Only here linesign is used as a tag
    var result=Tag.buildFrom(line)
    
    result = processTag(result, line)

    result.content += processChildren(line)
    result
  }
}