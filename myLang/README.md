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
- class OpExp
interface Stmt  
- class VardecStmt
- class LoopStmt
- class AssignStmt
interface Op  
- class PlusOp: 2
- class MinusOp: 3
- class LogicalAndOp: 4
- class LogicalOrOp: 5
- class LessThanOp: 6
class Program 