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

  def toHtml:String= {
    for(i <- Interpreter.interpreters) {
      if(i canInterpret this){
        return i.toHtml(this)
      }
    }

    return TagInterpreter.toHtml(this)
  }
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