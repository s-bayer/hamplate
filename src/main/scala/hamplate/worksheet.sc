package hamplate

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
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