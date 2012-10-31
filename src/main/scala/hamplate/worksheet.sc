package hamplate

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val lines = FileReader.readLines("./sample.hpt")//> lines  : Seq[String] = Stream(#nav, ?)

  val tokens = Tokenizer.tokenize(lines)          //> tokens  : Seq[hamplate.Token] = List(RestOfLine(#nav), hamplate.Newline@65ba
                                                  //| eab6, hamplate.Intendation@528786e7, LineType(%), RestOfLine(ul), hamplate.N
                                                  //| ewline@3fa9d205, hamplate.Intendation@261b66ea, hamplate.Intendation@5101a03
                                                  //| 1, LineType(%), RestOfLine(li), hamplate.Newline@41bc1a83, hamplate.Intendat
                                                  //| ion@2470b02c, hamplate.Intendation@623e2b64, hamplate.Intendation@5f92e49f, 
                                                  //| LineType(%), RestOfLine(a href = "#devices-tab"), hamplate.Newline@24dfb3be,
                                                  //|  hamplate.Intendation@5a9191db, hamplate.Intendation@afeef51, hamplate.Inten
                                                  //| dation@6711b47a, hamplate.Intendation@3b5fea73, RestOfLine(Devices), hamplat
                                                  //| e.Newline@16194279, hamplate.Intendation@545b2144, hamplate.Intendation@2ae5
                                                  //| 22a0, LineType(%), RestOfLine(li.active), hamplate.Newline@29444c60, hamplat
                                                  //| e.Intendation@42f247ca, hamplate.Intendation@3047411c, hamplate.Intendation@
                                                  //| 1603ae07, LineType(%), RestOfLine(a href = "#options-tab"), hamplate.Newline
                                                  //| @4b9658db, hamplate.Inte
                                                  //| Output exceeds cutoff limit.

  ASTBuilder.build(tokens).toHtml                 //> res0: String = "#nav
                                                  //| <ul>
                                                  //| <li>
                                                  //| <a href = "#devices-tab">
                                                  //| Devices
                                                  //| </a href = "#devices-tab">
                                                  //| </li>
                                                  //| <li.active>
                                                  //| <a href = "#options-tab">
                                                  //| System Options
                                                  //| </a href = "#options-tab">
                                                  //| </li.active>
                                                  //| <li>
                                                  //| <a href = "#reports-tab">
                                                  //| Reports
                                                  //| </a href = "#reports-tab">
                                                  //| </li>
                                                  //| <li>
                                                  //| <a href = "#notes-tab">
                                                  //| Notes
                                                  //| </a href = "#notes-tab">
                                                  //| </li>
                                                  //| </ul>
                                                  //| 
                                                  //| <section#intro>
                                                  //| <h1.page-header>
                                                  //| Sample page
                                                  //| </h1.page-header>
                                                  //| </section#intro>
                                                  //| 
                                                  //| <section#more-stuff>
                                                  //| Some more stuff
                                                  //| #stuff-list
                                                  //| .stuff
                                                  //| some stuff
                                                  //| .stuff
                                                  //| some more stuff
                                                  //| .stuff
                                                  //| even more stuff
                                                  //| </section#more-stuff>
                                                  //| 
                                                  //| <section#footer>
                                                  //| The footer
                                                  //| </section#footer>
                                                  //| 
                                                  //| <script type="text/javascript">
                                                  //| $(document).ready(function() {
                                                  //| $( "#dummy" ).dummy();
                                                  //| });
                                                  //| </script>
                                                  //| "

  println("finished")                             //> finished

}