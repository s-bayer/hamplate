package hamplate

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  FileManager.compile(".", ".")                   //> ./artifacts.xml
                                                  //| ./configuration
                                                  //| ./features
                                                  //| ./p2
                                                  //| ./eclipse
                                                  //| ./plugins
                                                  //| ./.eclipseproduct
                                                  //| ./eclipse.ini
                                                  //| ./libcairo-swt.so
                                                  //| ./readme
                                                  //| ./notice.html
                                                  //| ./epl-v10.html
                                                  //| ./icon.xpm
  val directory = new java.io.File(".");          //> directory  : java.io.File = .

  println("Current directory's canonical path: "
    + directory.getCanonicalPath());              //> Current directory's canonical path: /opt/scala-ide
  System.out.println("Current directory's absolute  path: "
    + directory.getAbsolutePath());               //> Current directory's absolute  path: /opt/scala-ide/.

  val lines = FileReader.readLines("./sample.hpt")//> lines  : Seq[String] = Stream(#nav, ?)

  val tokens = Tokenizer.tokenize(lines)          //> tokens  : Seq[hamplate.Token] = List(LineType(#), RestOfLine(nav), hamplate.
                                                  //| Newline@bd53a7c, hamplate.Intendation@5bf2a8f5, LineType(%), RestOfLine(ul),
                                                  //|  hamplate.Newline@18329bfc, hamplate.Intendation@229ec9cd, hamplate.Intendat
                                                  //| ion@66d2c37c, LineType(%), RestOfLine(li), hamplate.Newline@172036a1, hampla
                                                  //| te.Intendation@43e28fe3, hamplate.Intendation@4f54a2e6, hamplate.Intendation
                                                  //| @3d92d2ba, LineType(%), RestOfLine(a href = "#devices-tab"), hamplate.Newlin
                                                  //| e@7248d0ea, hamplate.Intendation@a8bed44, hamplate.Intendation@688610c4, ham
                                                  //| plate.Intendation@39b6e978, hamplate.Intendation@1799e2e2, RestOfLine(Device
                                                  //| s), hamplate.Newline@77fe6f88, hamplate.Intendation@614c8743, hamplate.Inten
                                                  //| dation@6744719c, LineType(%), RestOfLine(li.active), hamplate.Newline@3e7eed
                                                  //| bb, hamplate.Intendation@7f29b00a, hamplate.Intendation@9e4a1bf, hamplate.In
                                                  //| tendation@13707ac, LineType(%), RestOfLine(a href = "#options-tab"), hamplat
                                                  //| e.Newline@43d8add3, hamp
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