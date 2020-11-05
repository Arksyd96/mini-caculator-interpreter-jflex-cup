# Intro

The objective of this project is to develop a "calculator" that handles integers as well as
arrays (or lists) of integers using JFlex and Java cup. The calculator must know how to recognize
and evaluate expressions using only addition and multiplication operators. 
It must also recognize arrays of integers with a possibility of an indexed access and
concatenation of arrays. Finally, the calculator must be able to make integer assignments
and arrays in variables.

# How to use
The project also contains a makefile with the following three functions:
1. make : generates the .java files for the lexer and parser and to compile the them.
2. make parse : launches the interpreter.
3. make test : runs some unit tests on the interpreter, JUnit 4 was used to realize the tests.

# Interpreter limits
The use of arrays in an arithmetic expression are not accepted (this was inspired from the JVM interpreter, it's more a design choice than a limitation):
```bash
> 5 + 2 ∗ {1 , 2 , 3}
> Erreur syntaxique sur le caractère {
```
however, it is possible to write expressions with type variables ``Tableau``, but this will have the effect of returning a semantic error:
```bash
> 5 + 2 ∗ @tab
> Erreur sémantique −> types incompatible ∗
```
