#hamplate

Insanely productive template engine for scala projects.

## Important ##
Hamplate at the moment is in early alpha, so syntax rules might change.


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

## Syntax

### Basic Syntax ###
Hamplate borrows major parts from slim and jade.
So it's basic syntax rules are the same.
So this:

    !!!
    html
      head
      body
        h1 Headline
        p
          em Lorem ipsum ...

renders to


    <!DOCTYPE html>
    <html>
      <head />
      <body>
          <h1> Headline </h1>
          <p>
            <em> Lorem ipsum ... </em>
          </p>
      </body>
    </html>

### Design philosophy ###
#### Less concepts vs terse syntax ####
*There should only be a very limited amount of concepts*

*You should only need to type as few characters as possible*

To achieve those goals there are some basic concept used in hamplates
* The first symbol in a line (or the absence of such) determines how this line is interpreted
* Besides that there is only one special symbol: The *@*-sign. It is used to escape scala code
* You should not need to use brackets ever. Not even in scala constructs.

### Variables and Logic ###
You can use any scala code in your templates.
Scala code is started with an *@*-sign, like in

    @import views.helpers.myhelper
    @myvar=3
    @list.foreach@{ a => a.toUpper }
    @myclass.mymethod@{myvar}
    @myclass.mymethod(myvar)

The second @ in the third and fourth line is needed, since everything between *{}* is treated as template code.
If you don't want to have something evaluated as template code, you can use *()* instead.

### Control flow ###
The basic control structures in scala are *if*, *for* and *match*

Hamplate compiles down to scala code (or play templates, which compile to scala code),
so all control structures are like the scala ones. Except *unless*, which renders like *if not*.

TODO match syntax

All control structures need to be on their own line in the template document.


#### if and unless ####
    - if 3 > 2
      em true
      
    - if 2 > 3
      em false
    - else
      em true
      
    - unless 3 > 2
      em true
    - else
      em false
      
    - unless 2 > 3
      em false
renders to

    <em> true </em>
    <em> true </em>
    <em> false </em>
    <em> false </em>
