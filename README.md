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


