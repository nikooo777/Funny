# Funny 
A java-based programming language 
 
 
#Grammar 
program ::= function Eos . 
 
function ::= "{" optParams optLocals optSequence "}" . 
optParams ::= ( "(" optIds ")" )? . 
optLocals ::= optIds . 
optSequence ::= ( "->" sequence )? . 
optIds::= ( id ( "," id )* )? . 
id ::= Id . 
 
sequence ::= optAssignment ( ";" optAssignment )* . 
optAssignment := assignment? . 
assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment 
	| logicalOr . 
 
logicalOr ::= logicalAnd ( "||" logicalOr )? . 
logicalAnd ::= equality ( "&&" logicalAnd )? . 
equality ::= comparison ( ( "==" | "!=" ) comparison )? . 
comparison ::= add ( ( "<" | "<=" | ">" | ">=" ) add )? . 
add ::= mult ( ( "+" | "-" ) mult )* . 
mult ::= unary ( ( "*" | "/" | "%" ) unary )* . 
unary ::= ( "+" | "-" | "!" ) unary 
	| postfix . 
 
postfix ::= primary args* . 
args ::= "(" ( sequence ( "," sequence )* )? ")" . 
primary ::= num | bool | nil | string 
	| getId 
	| function 
	| subsequence 
	| cond 
	| loop 
	| print . 
 
num ::= Num . 
bool ::= True | False . 
nil ::= Nil . 
string ::= String . 
getId ::= Id . 
subsequence ::= "(" sequence ")" . 
cond ::= ( "if" | "ifnot" ) sequence "then" sequence ( "else" sequence )? "fi" . 
loop ::= ( "while" | "whilenot" ) sequence ( "do" sequence )? "od" . 
print ::= ( "print" | "println" ) args .