# myLang #  

Grammer Definition  

var is a Variable  
num is a Number  
type ::= 'int' | 'bool'  
vardec ::= '(' 'vardec' type var expression ')'  
expression ::= var | num | 'true' | 'false' | '(' op expression expression ')'  
loop ::= '(' 'while' expression statement ')'  
assign ::= '(' '=' var expression ')'  
statement ::= vardec | loop | assign  
op ::= '+' | '-' | '&&' | '||' | '<' | ...  
program ::= statement*  

This will get compiled to javascript  

# Tokens #  

- IdentifierToken(String)  
- NumberToken(int)  
- IntToken  (Hashcode: 0)
- BoolToken  1
- LeftParenToken  2
- VardecToken  3
- RightParenToken  4
- TrueToken  5
- FalseToken  6
- WhileToken  7
- SingleEqualsToken  8
- PlusToken  9
- MinusToken  10
- LogicalAndToken  11
- LogicalOrToken  12
- LessThanToken  13

# AST Definition #

interface Type  
- class IntType: (hashcode 0)
- class BoolType: (hashcode only for node that dont have children)  
interface Exp  
- class NumberLiteralExp
- class BooleanLiteralExp
- class VariableExp
- class BinaryOperatorExp
interface Stmt  
- class VardecStmt
- class WhileStmt
- class AssignStmt
interface Op  
- class PlusOp: 2
- class MinusOp: 3
- class LogicalAndOp: 4
- class LogicalOrOp: 5
- class LessThanOp: 6  
class Program 

# Types #
- Vardec puts a variable in scope with a type  
  - Need to remember the variable and the type  
  - Need to ensure the expression is of the type  
- num should be an int  
- true and false should be bools  
- var is whatever the type of the variable is  
- while's expression is a boolean  
- assign:  
  - var should be in scope  
  - var's type should match expression's type  
- exp1 + exp2 => int, exp1: int, exp2: int  
- exp1 - exp2 => int, exp1: int, exp2: int
- exp1 && exp2 => bool, exp1: bool, exp2: bool
- exp1 || exp2 => bool, exp1: bool, exp2: bool
- exp1 < exp2 => bool, exp1: int, exp2: int
- program: all statements are well typed