#hamplate

Insanely productive template engine for scala projects

## Important ##
Hamplate at the moment is in the design phase and some time away from a running implementation.


## Why another template engine? ##
There are quite a few great template engines out there, like jade or haml.
But those are not statically typed.


The only statically typed template engine I know of is the playframework2 template engine.
But the Play! engine does not help you deal with the HTML/XML hazzle. You still have to close tags and write class="foobar" statements.


Hamplate tries to make writing view code fun again (like jade does).
Aditionally it gives you a safety net in form of the compiler and maybe some automated tests.

It also tries to give sensible defaults for dealing with forms, helpers and CSS/JS includes, when using playframework2.


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
There should only be one character you should need to escape. The *@*-sign.

The other special characters have to be used at special places - like the start of a line - to be treated special.

### Variables and Logic ###
TODO


### Control flow ###
The basic control structures in scala are *if*, *for* and *match*

TODO match syntax

All control structures need to be on their own line in the template document.


#### if and unless ####
    - if 3 > 2
      em true
    - if 2 > 3
      em false
    - unless 3 > 2
      em true
    - unless 2 > 3
      em false
renders to

    <em> true </em>
    <em> false </em>
