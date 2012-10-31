package hamplate

sealed abstract class AST {
  def insert(originalIntendation: Int, intendation: Int, content: String, builder: Builder): AST =
    if (intendation < 0)
      throw new Error("intendation < 0")
    else if (intendation == 0) {
      children = children :+ builder(originalIntendation, content)
      this
    } else {
      children.last.insert(originalIntendation, intendation - 1, content, builder)
      this
    }

  var children: Seq[AST] = Seq()

  def childrenToHtml: String = {
    children.map(_ toHtml).mkString
  }

  def toHtml: String

  def indent(intendation: Int): String =
    if (intendation == 0) ""
    else "  " + indent(intendation - 1)
}

class Root extends AST {
  override def toString = children.mkString("[", ",", "]")
  override def toHtml: String =
    childrenToHtml
}

class TextNode(intendation: Int, val content: String) extends AST {
  override def toString = children.mkString("{", ",", "}")
  override def toHtml: String =
    if (children.nonEmpty)
      indent(intendation) + content + "\n" +
        childrenToHtml
    else
      indent(intendation) + content + "\n"
}

class FilterNode(intendation: Int, val content: String) extends AST {
  // TODO SB check that all children are TextNodes or maybe comment nodes
  override def toHtml: String =
    if (content.trim == "javascript")
      indent(intendation) + "<script type=\"text/javascript\">\n" +
        childrenToHtml +
        indent(intendation) + "</script>" + "\n"
    else throw new Error("unknown filter type")
}

class TagNode(intendation: Int, val content: String) extends AST {
  // TODO SB interpret .class #id and ignore attributes in closing tag
  override def toHtml: String = {
    /*val tagR = """([^.#\s]+)"""
    val remainingR = """.*"""
    val TagRegex = (tagR + remainingR).r
    val tag = content match {
      case TagRegex(c) => c
      case _ => throw new Error("could not match the tag inside the TagNode '" + content + "'")
    }*/

    val TagRegex = ("""([^.#\s]+)([^\s]*)(.*)""").r
    val (tag, classAndId, attributes) = content match {
      case TagRegex(t, c, a) => (t, c, a)
    }

    val ids = """(#[^.#\s]+)""".r.findAllIn(classAndId).toList
      .map(_.tail).mkString.trim // strip the "#"-sign
    val classes = """(\.[^.#\s]+)""".r.findAllIn(classAndId).toList
      .map(_.tail).mkString.trim // strip the "."-sign

    val idPart = if (ids.isEmpty) "" else " id=\"" + ids + "\""
    val classPart = if (classes.isEmpty) "" else " class=\"" + classes + "\""

    indent(intendation) + "<" + tag + idPart + classPart + attributes + ">\n" +
      childrenToHtml +
      indent(intendation) + "</" + tag + ">\n"
  }
}

sealed abstract class Builder {
  def apply(intendation: Int, c: String): AST
}

object TextBuilder extends Builder {
  override def apply(intendation: Int, c: String) = new TextNode(intendation, c)
}

object FilterBuilder extends Builder {
  override def apply(intendation: Int, c: String) = new FilterNode(intendation, c)
}

object TagBuilder extends Builder {
  override def apply(intendation: Int, c: String) = new TagNode(intendation, c)
}

object ASTBuilder {
  private def makeAST(currentAst: AST, remaining: Seq[Token], builder: Builder = TextBuilder, intendation: Int = 0): AST = remaining match {
    case x :: xs => x match {
      case x: Newline =>
        // Reset intendation
        makeAST(currentAst, xs)
      case x: Intendation =>
        makeAST(currentAst, xs, builder, intendation + 1)
      case LineType(c) => c match {
        case ":" => makeAST(currentAst, xs, FilterBuilder, intendation)
        case "%" => makeAST(currentAst, xs, TagBuilder, intendation)
        case _ => throw new Error("unknown LineType(" + c + ")")
      }
      case RestOfLine(c) =>
        makeAST(currentAst.insert(intendation, intendation, c, builder), xs, builder, intendation)
    }
    case _ =>
      currentAst
  }

  def build(tokens: Seq[Token]): AST =
    makeAST(new Root, tokens)
}