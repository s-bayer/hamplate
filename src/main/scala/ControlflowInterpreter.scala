object ControlflowInterpreter extends Interpreter {
  override def sign = "-"

  override def toHtml(line: Line):String = {
    var res = line.prepend
    val stmt = new Statement(line)
    stmt.kind match {
      case 'if => {
        res += "@"+stmt.keyword+"("+stmt.arguments+") {\n"
      } case 'unless => {
        res += "@"+"if"+"(!("+stmt.arguments+")) {\n"
      }case 'else => {
        res += stmt.keyword+" {\n"
      } case 'for => {
        res += "@"+stmt.keyword+"("+stmt.arguments+") {\n"
      }case _ => {
        throw new Exception("Unknown keyword: '"+stmt.keyword+"'")
      }
    }
    res += processChildren(line)
    res += line.prepend+"}\n"
    res
  }
}

private class Statement(line: Line){
  var keyword:String = _
  var arguments:String = _
  var kind:Symbol = _

  init()

  private def init() = {
    val withoutDash = line.value.split("-").tail.mkString("-").trim
    keyword = withoutDash.split(" ").head
    arguments = withoutDash.split(" ").tail.mkString(" ").trim
    kind = Symbol(keyword)
  }
}