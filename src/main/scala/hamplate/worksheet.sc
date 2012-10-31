package hamplate

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val lines = FileReader.readLines("./sample.hpt")//> lines  : Seq[String] = Stream(#nav, ?)

  val tokens = Tokenizer.tokenize(lines)          //> tokens  : Seq[hamplate.Token] = List(RestOfLine(#nav), hamplate.Newline@5287
                                                  //| 86e7, hamplate.Intendation@3fa9d205, LineType(%), RestOfLine(ul), hamplate.N
                                                  //| ewline@261b66ea, hamplate.Intendation@5101a031, hamplate.Intendation@41bc1a8
                                                  //| 3, LineType(%), RestOfLine(li), hamplate.Newline@2470b02c, hamplate.Intendat
                                                  //| ion@623e2b64, hamplate.Intendation@5f92e49f, hamplate.Intendation@24dfb3be, 
                                                  //| LineType(%), RestOfLine(a href = "#devices-tab"), hamplate.Newline@5a9191db,
                                                  //|  hamplate.Intendation@afeef51, hamplate.Intendation@6711b47a, hamplate.Inten
                                                  //| dation@3b5fea73, hamplate.Intendation@16194279, RestOfLine(Devices), hamplat
                                                  //| e.Newline@545b2144, hamplate.Intendation@2ae522a0, hamplate.Intendation@2944
                                                  //| 4c60, LineType(%), RestOfLine(li.active), hamplate.Newline@42f247ca, hamplat
                                                  //| e.Intendation@3047411c, hamplate.Intendation@1603ae07, hamplate.Intendation@
                                                  //| 4b9658db, LineType(%), RestOfLine(a href = "#options-tab"), hamplate.Newline
                                                  //| @7ba3b8a1, hamplate.Inte
                                                  //| Output exceeds cutoff limit.

  ASTBuilder.build(tokens).toHtml                 //> res0: String = "#nav
                                                  //|   <ul>
                                                  //|     <li>
                                                  //|       <a href = "#devices-tab">
                                                  //|         Devices
                                                  //|       </a href = "#devices-tab">
                                                  //|     </li>
                                                  //|     <li.active>
                                                  //|       <a href = "#options-tab">
                                                  //|         System Options
                                                  //|       </a href = "#options-tab">
                                                  //|     </li.active>
                                                  //|     <li>
                                                  //|       <a href = "#reports-tab">
                                                  //|         Reports
                                                  //|       </a href = "#reports-tab">
                                                  //|     </li>
                                                  //|     <li>
                                                  //|       <a href = "#notes-tab">
                                                  //|         Notes
                                                  //|       </a href = "#notes-tab">
                                                  //|     </li>
                                                  //|   </ul>
                                                  //| 
                                                  //| <section#intro>
                                                  //|   <h1.page-header>
                                                  //|     Sample page
                                                  //|   </h1.page-header>
                                                  //| </section#intro>
                                                  //| 
                                                  //| <section#more-stuff>
                                                  //|   Some more stuff
                                                  //|   #stuff-list
                                                  //|     .stuff
                                                  //|       some stuff
                                                  //|     .stuff
                                                  //|       some more stuff
                                                  //|     .stuff
                                                  //|       even more stuff
                                                  //| </section#more-stuff>
                                                  //| 
                                                  //| <section#footer>
                                                  //|   The footer
                                                  //| </section#footer>
                                                  //| 
                                                  //| <script type="text/javascript">
                                                  //|   $(document).ready(
                                                  //| Output exceeds cutoff limit.

  println("finished")                             //> finished

}