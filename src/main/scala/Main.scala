object Main extends Application {
  println(Tree.buildFrom(Sample.text).toHtml)
}

object Sample {
  val text="""html
  head
  / my comment
    and it's children
    which should not show up
  body
    em Some text
    h2 Headline
    form
      input type=text
      a href='www.example.com' @ The example"""
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
  override def sign = ""
  override def toHtml(line: Line):String = {
    if(line.hasChildren) {
      renderWithChildren(line)
    } else {
      renderWithoutChildren(line)
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

  def renderWithoutChildren(line:Line):String = {
    var result="  "*line.depth

    tailofline(line) match {
      case Some(tail) => {
        result += "<"+linesign(line)+debug(line)+">"
        result += " "+tail.trim+" "
        result += "</"+linesign(line)+">\n"
      }
      case _ => {
        result += "<"+linesign(line)+debug(line)+"/>\n"
      }
    }
    result
  }

  def renderWithChildren(line: Line):String = {
    // Only here linesign is used as a tag
    var result=("  "*line.depth)+"<"+linesign(line)+debug(line)+">\n"
    tailofline(line) match {
      case Some(tail) => result += ("  "*(line.depth+1))+tail.trim + "\n"
      case _ => ()
    }
    result += processChildren(line)
    result += ("  "*line.depth)+"</"+linesign(line)+">\n"
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