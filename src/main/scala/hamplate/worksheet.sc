package hamplate

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  FileReader.test("./")                           //> /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/Main.class
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/Line$$anonfun$clean$1.class
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/Leaf.class
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/hamplate
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/TagInterpreter$Tag$$anonfun$3.class
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/ControlflowInterpreter$.class
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/CommentInterpreter.class
                                                  //| /home/sebastian/seb/freizeit/hobbies/programmieren/projects/hamplate/target/
                                                  //| scala-2.9.2/classes/Interprete
                                                  //| Output exceeds cutoff limit.
  FileReader.readLines("./sample.hpt")            //> #nav
                                                  //|   %ul
                                                  //|     %li
                                                  //|       %a href = "#devices-tab"
                                                  //|         Devices
                                                  //|     %li.active
                                                  //|       %a href = "#options-tab"
                                                  //|         System Options
                                                  //|     %li
                                                  //|       %a href = "#reports-tab"
                                                  //|         Reports
                                                  //|     %li
                                                  //|       %a href = "#notes-tab"
                                                  //|         Notes
                                                  //| 
                                                  //| %section#intro
                                                  //|   %h1.page-header
                                                  //|     Sample page
                                                  //| 
                                                  //| %section#more-stuff
                                                  //|   Some more stuff
                                                  //|   #stuff-list
                                                  //|     .stuff
                                                  //|       some stuff
                                                  //|     .stuff
                                                  //|       some more stuff
                                                  //|     .stuff
                                                  //|       even more stuff
                                                  //| 
                                                  //| %section#footer
                                                  //|   The footer
                                                  //| 
                                                  //| :javascript
                                                  //|   $(document).ready(function() {
                                                  //|   $( "#dummy" ).dummy();
                                                  //|   });
  
  println("finished")                             //> finished
  
}