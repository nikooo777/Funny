# Funny<br />
A java-based programming language<br />
<br />
<br />
#Grammar<br />
program ::= function Eos .<br />
<br />
function ::= "{" optParams optLocals optSequence "}" .<br />
optParams ::= ( "(" optIds ")" )? .<br />
optLocals ::= optIds .<br />
optSequence ::= ( "->" sequence )? .<br />
optIds::= ( id ( "," id )* )? .<br />
id ::= Id .<br />
<br />
sequence ::= optAssignment ( ";" optAssignment )* .<br />
optAssignment := assignment? .<br />
assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment<br />
	| logicalOr .<br />
<br />
logicalOr ::= logicalAnd ( "||" logicalOr )? .<br />
logicalAnd ::= equality ( "&&" logicalAnd )? .<br />
equality ::= comparison ( ( "==" | "!=" ) comparison )? .<br />
comparison ::= add ( ( "<" | "<=" | ">" | ">=" ) add )? .<br />
add ::= mult ( ( "+" | "-" ) mult )* .<br />
mult ::= unary ( ( "*" | "/" | "%" ) unary )* .<br />
unary ::= ( "+" | "-" | "!" ) unary<br />
	| postfix .<br />
<br />
postfix ::= primary args* .<br />
args ::= "(" ( sequence ( "," sequence )* )? ")" .<br />
primary ::= num | bool | nil | string<br />
	| getId<br />
	| function<br />
	| subsequence<br />
	| cond<br />
	| loop<br />
	| print .<br />
<br />
num ::= Num .<br />
bool ::= True | False .<br />
nil ::= Nil .<br />
string ::= String .<br />
getId ::= Id .<br />
subsequence ::= "(" sequence ")" .<br />
cond ::= ( "if" | "ifnot" ) sequence "then" sequence ( "else" sequence )? "fi" .<br />
loop ::= ( "while" | "whilenot" ) sequence ( "do" sequence )? "od" .<br />
print ::= ( "print" | "println" ) args .