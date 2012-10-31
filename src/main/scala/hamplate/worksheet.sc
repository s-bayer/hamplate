package hamplate

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val lines = FileReader.readLines("./sample.hpt")//> lines  : Seq[String] = Stream(#nav, ?)

  val tokens = Tokenizer.tokenize(lines)          //> tokens  : Seq[hamplate.Token] = List(LineType(#), RestOfLine(nav), hamplate.
                                                  //| Newline@65baeab6, hamplate.Intendation@528786e7, LineType(%), RestOfLine(ul)
                                                  //| , hamplate.Newline@3fa9d205, hamplate.Intendation@261b66ea, hamplate.Intenda
                                                  //| tion@5101a031, LineType(%), RestOfLine(li), hamplate.Newline@41bc1a83, hampl
                                                  //| ate.Intendation@2470b02c, hamplate.Intendation@623e2b64, hamplate.Intendatio
                                                  //| n@5f92e49f, LineType(%), RestOfLine(a href = "#devices-tab"), hamplate.Newli
                                                  //| ne@24dfb3be, hamplate.Intendation@5a9191db, hamplate.Intendation@afeef51, ha
                                                  //| mplate.Intendation@6711b47a, hamplate.Intendation@3b5fea73, RestOfLine(Devic
                                                  //| es), hamplate.Newline@16194279, hamplate.Intendation@545b2144, hamplate.Inte
                                                  //| ndation@2ae522a0, LineType(%), RestOfLine(li.active), hamplate.Newline@29444
                                                  //| c60, hamplate.Intendation@42f247ca, hamplate.Intendation@3047411c, hamplate.
                                                  //| Intendation@1603ae07, LineType(%), RestOfLine(a href = "#options-tab"), hamp
                                                  //| late.Newline@4b9658db, h
                                                  //| Output exceeds cutoff limit.

  ASTBuilder.build(tokens).toHtml                 //> res0: String = "<div id="nav">
                                                  //|   <ul>
                                                  //|     <li>
                                                  //|       <a href = "#devices-tab">
                                                  //|         Devices
                                                  //|       </a>
                                                  //|     </li>
                                                  //|     <li class="active">
                                                  //|       <a href = "#options-tab">
                                                  //|         System Options
                                                  //|       </a>
                                                  //|     </li>
                                                  //|     <li>
                                                  //|       <a href = "#reports-tab">
                                                  //|         Reports
                                                  //|       </a>
                                                  //|     </li>
                                                  //|     <li>
                                                  //|       <a href = "#notes-tab">
                                                  //|         Notes
                                                  //|       </a>
                                                  //|     </li>
                                                  //|   </ul>
                                                  //| </div>
                                                  //| 
                                                  //| <section id="intro">
                                                  //|   <h1 class="page-header">
                                                  //|     Sample page
                                                  //|   </h1>
                                                  //| </section>
                                                  //| 
                                                  //| <section id="more-stuff">
                                                  //|   Some more stuff
                                                  //|   <div id="stuff-list">
                                                  //|     <div class="stuff">
                                                  //|       some stuff
                                                  //|     </div>
                                                  //|     <div class="stuff">
                                                  //|       some more stuff
                                                  //|     </div>
                                                  //|     <div class="stuff">
                                                  //|       even more stuff
                                                  //|     </div>
                                                  //|   </div>
                                                  //| </section>
                                                  //| 
                                                  //| <section id="footer" class="bottom">
                                                  //|   The footer
                                                  //| </section>
                                                  //| 
                                                  //| <script typ
                                                  //| Output exceeds cutoff limit.

  println("finished")                             //> finished

}