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
  /**
    * Override me
    **/
  def sign: String
  def toHtml(l: Line): String
}

object Interpreter {
  def interpreters = Seq(CommentInterpreter, ControlflowInterpreter)
}