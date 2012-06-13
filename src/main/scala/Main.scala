object Main extends Application {
  println(Tree.buildFrom(Sample.text).toHtml)
}

object Sample {
  val text="""html
  head
  / my comment
    and it's children
    which should not show up
  body content="test" Testtext
    em Some text
    h2 Headline
    / Another comment
    form.class1.class2#id.class3 class="existingclass"
      input#myid.class.class2 type='text'
      a href="www.example.com" class="test" The example"""
  // TODO Throw error, wenn a attribute value has the form "bla' or 'bla"
  // TODO Allow omitting '' and "" on attribute values
}

object Tree {
  def buildFrom(string: String): Line = {
    new Line(string, None)
  }
}

class Line(text: String, parent: Option[Tree[String]]) extends Node[String](parent) {
  override def value = {
    text.split("\n").head.trim
  }

  def hasChildren:Boolean = {
    children != null && children.size > 0
  }

  def intendation = {
    val untrimmed = text.split("\n").head
    untrimmed.indexWhere(_ != ' ')
  }

  override var children : Seq[Tree[String]]= {
    text.split("\n").tail.map(new Line(_, Some(this)))
  }

  def clean {
    val neededIntendation = (depth+1)*2
    var lastItem : Option[Line]= None
    for(c <- children) {
      // TODO Weird cast. Remove if possible
      val l = c.asInstanceOf[Line]
      if(neededIntendation != l.intendation) {
        lastItem match {
          case None => throw new Exception("Intendation error on child "+l.value+".\n"+
            "\tintendation is "+l.intendation+" but should be "+neededIntendation)
          case Some(line) => {
            line.addChild(l)
            line.clean
          }
        }
      } else {
        lastItem = Some(l)
      }
    }
  }

  clean

  def interpreters = Seq(CommentInterpreter)

  def toHtml:String= {
    for(i <- interpreters) {
      if(i canInterpret this){
        return i.toHtml(this)
      }
    }

    return TagInterpreter.toHtml(this)
  }
}

object CommentInterpreter extends Interpreter {
  override def sign = "/"
  override def toHtml(line: Line):String = {
    ""
  }
}

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

  def debug(line:Line):String = {
    return ""
    val deb = line.parent match {
      case Some(p) => p.asInstanceOf[Line].value
      case _ => "-"
    }
    "("+deb+")"
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

abstract class Interpreter {
  def processChildren(line: Line): String= {
    var result = ""
    for(c <- line.children) {
      // TODO Weird cast. Remove if possible
      val l = c.asInstanceOf[Line]
      result += l.toHtml
    }
    result
  }
  def linesign(line:Line):String = {
    val Regex = """(\s*)([^\s]+)(()|(.+))""".r
    val Regex(_,lsign,_*) = line.value
    lsign
  }
  def tailofline(line:Line):Option[String] = {
    val Regex = """(\s*)([^\s]+)(()|(.+))""".r
    val Regex(_,_,_,_,tail) = line.value
    Option(tail)
  }
  def canInterpret(line:Line): Boolean = {
    linesign(line:Line) == sign
  }
  def sign: String
  def toHtml(l: Line): String
}

class Tree[A](var parent: Option[Tree[A]]) {
  def depth: Int = parent match {
    case None => 0
    case Some(p) => 1 + p.depth
  }
}

class Leaf[A](parent: Option[Tree[A]]) extends Tree[A](parent) {
  override def toString = "*"
}

abstract class Node[A](parent: Option[Tree[A]]) extends Tree[A](parent){
  var children: Seq[Tree[A]]
  def value : A

  def removeChild(a: Tree[A]) {
    val index: Int= children.indexWhere(_ == a)
    val x = children.splitAt(index)
    children = x._1 ++ x._2.tail
  }

  def addChild(a: Tree[A]) {
    children = children :+ a
    a.parent match {
      case Some(p) => p match {
        case n:Node[A] => n.removeChild(a)
      }
      case _ => ()
    }
    a.parent = Some(this)
  }

  override def toString() = {
    var result = ("  "*depth)+value+"\n"
    val childs = for(c <- children) {
      result += c.toString
    }
    result
  }
}