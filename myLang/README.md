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
- IntToken  
- BoolToken  
- LeftParenToken  
- RightParenToken  
- VardecToken  
- TrueToken  
- FalseToken  
- WhileToken  
- SingleEqualsToken  
- PlusToken  
- MinusToken  
- LogicalAndToken  
- LogicalOrToken  
- LessThanToken  