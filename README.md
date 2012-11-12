#hamplate

Insanely productive template engine for scala projects.

## Important ##
Hamplate is in early alpha, so syntax rules might change.

## Why another template engine? ##
There are quite a few great template engines out there, like jade, slim or haml.
But those are not statically typed.


The only statically typed template engine I know of is the playframework2 template engine.
But the Play! engine does not help you deal with the HTML/XML hazzle.
You still have to write stuff like this:

    <ul id="dummies" class="sorted">
      <li>
        <a class="dummy-link" href="...">
          <span class="hidden dummy-name">a dummy name</a>
          <img src="..." />
        </a>
      </li>
    </ul>

I'm used to write (and more important read) extremly concise code in scala and want to be able to just write:

    %ul#dummies.sorted
      %li
        %a.dummy-link href="..."
          %span.hidden.dummy-name
            a dummy name
          %img src="..."

This is roughly two thirds of the original code and avoids most unnecessary duplication.

So in summary Hamplate tries to make writing view code fun again (like haml does).

And since it's not actually a full template engine but more a template transformer, it compiles down to statically typed Play! templates.

In the future it might also give you a safety net in form of the compiler and maybe some automated checks.

There will also be some sensible defaults for dealing with forms, helpers and CSS/JS includes, and embedded scala code.

## Installation ##

Just copy this in your build.sbt

	seq(hamplateSettings:_*)
	
	unmanagedResourceDirectories in Compile <+= (baseDirectory) { _ / "app/views/" }

And this in "project/plugins.sbt"
	
	addSbtPlugin("syrix" % "hamplate" % "0.1.0")

After doing this just type

	sbt ~hpt

and all your *.hpt files in app/views will be automatically compiled to *.scala.html files everytime you change a file.

*Be carefull not to overwrite your *.scala.html files by accident!* Happend to me. Really hurts...

## Syntax

### Basic Syntax ###
Hamplate borrows major parts from haml.
So it's basic syntax rules are the same.
So this:

    %html
      %head
      %body
        %h1
          Headline
        %p
          %em
            Lorem ipsum ...

renders to


    <html>
      <head>
      </head>
      <body>
        <h1>
          Headline
        </h1>
        <p>
          <em>
            Lorem ipsum ...
          </em>
        </p>
      </body>
    </html>

### Design philosophy ###
#### Less concepts vs terse syntax ####
*There should only be a very limited set of concepts*

*You should only need to type as few characters as possible*

*The compiler should be as simple as possible*

To achieve those goals there are some basic concept used in hamplates
* The first symbol in a line (or the absence of such) determines how this line is interpreted
* Besides that there are no special symbols
* You should not need to use brackets ever. Not even in scala constructs.
* You should be able to bypass those rules when needed and just write plain html

### Syntax rules ###
#### Line interpretation ####
There are only four kinds of special symbols at the moment: '%','.','#',':'.

All lines not starting with one of this symbols (after the intendation) is passed through the compiler as it was.
So this html snippet does not change through hamplate:

    <h3> Header </h3>
    <p> Lorem ipsum <p>
    <p>
      @content
    </p>


##### Html-Tags ('%') #####
Lines starting with % are the most complex to parse and indicate an html tag.

As example this:

    %a.myclass1#myid.myclass2 href="..." data-toggle="..."
      random link

Get's parsed to

    <a class="myclass1 myclass2" id="myid" href="..." data-toggle="...">
      random link
    </a>

The parsing rules are

*Everything from the %-sign to the first occurrence of '#', '.' or whitespace is parsed as the tag name.*

This name is used in the opening and closing tag.
The closing tag is put after the last line which has a higher intendation than the line where the tag starts.

*Everything from the first occurrecne of '#' or '.' is parsed as class and id attributes.*

A dot indicates a class and a hash indicates an id.
If you don't need to assign classes or ids (or want to do it the traditional way, using id="myid"), you don't need to use class or id shorthands.
But the name of the tag must be present after the %-Symbol.
So this is not valid hamplate:
    
    %

*The rest of the line is simply moved into the starting tag.*

There is no magic going on, so this.

    %a.class1 class="class2" some random stuff
    
renders to

    <a class="class1" class="class2" some random stuff>
    </a>

So as you see the parser is really really dumb and you can't write certain things like

    %h1 Lorem ipsum

Which will render to

	<h1 Lorem ipsum>
	</h1>
    
But must write
    
    %h1
      Lorem ipsum
    
This may seem silly, but it's way easier to understand, what the compiler does.
It just onfolds the class and id shortcuts and copies the remaining line in the starting tag.

Braindead simple and way easier to read than HTML.

##### Divs with class or id attributes ('.','#') #####

Lines starting with a dot or hash are exactly interpreted as lines starting with an additional '%div'

So this

    #navbar.myclass
      .item
        Item one
      .item
        Item two

renders exactly like this

    %div#navbar.myclass
      %div.item
        Item one
      %div.item
        Item two

which renders to

    <div id="navbar" class="myclass">
      <div class="item">
        Item one
      </div>
      <div class="item">
        Item two
      </div>
    </div>

##### Filters (':') #####

Filters are used to make writing embedded code (like javascript or markdown) easier.

At the moment there is only one filter. It is used for javascript and works like this:

    :javascript
      alert('hello world!");

renders to

    <script type="text/javascript">
      alert('hello world!");
    </script>

Closing rules of filters are the same as for tags.

*Everything inside a filter is still parsed as hamplate code*

So you should never use '%', '.', ':' or '#' at the start of a line inside of a filter.
At the moment the compiler will just happily transform your code and mess up your script tag.
Always remember: The compiler is really really dumb.

In the future there might be compiler errors or warnings to indicate the use of special symbols inside of a filter.

##### Embedded scala #####
Lines starting with '{', '@' or '}' will not be evalualted by the parser (they are normal text-lines).
So you can just write normal Play! template code and the Play!-Compiler will do the heavy lifting and compile your code.

Example:

    %ul
      @for(u <- user) {
      %li
        %h2
          @u.name <small>@u.email</small>
        %p
          @u.description
	  }

renders to

	<ul>
	  @for(u <- user) {
	  <li>
	    <h2>
	      @u.name <small>@u.email</small>
	    </h2>
	    <p>
	      @u.description
	    </p>
	  </li>
	  }
	</ul>

### Future improvements ###

In the future there might be some shorthands to support common scala constructs.
Like '-', which puts an @sign at the front of the line, a opening '{' at the end of the line and a closing '}' after the last line with higher intendation.

So this

	-for(u <- user)
	  @u.name

might render to

	@for(u<-user){
	  @u.name
	}

There might also be stuff like autoimports or better support of pattern matching.

But that's for some later version. For now I'm just happy to never have to write HTML-tags again ;-)