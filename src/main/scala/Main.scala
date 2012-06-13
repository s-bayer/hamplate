object Main extends Application {
  println(Tree.buildFrom(Sample.text).toHtml)
}

// TODO Add support for empty lines
object Sample {
  val text="""html
  head
  / my comment
    and it's children
    which should not show up
  body content="test" Testtext
    em Some text
    h2 Headline
    / Another comment
    / 
    - if a > 3
      h2 yay
    form.class1.class2#id.class3 class="existingclass"
      input#myid.class.class2 type='text'
      a href="www.example.com" class="test" The example"""
  // TODO Throw error, wenn a attribute value has the form "bla' or 'bla"
  // TODO Allow omitting '' and "" on attribute values
}
