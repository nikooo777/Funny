/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import funny.Token.Type;

class Compiler
{

	private final Tokenizer tokenizer;

	Compiler(Reader in) throws IOException
	{
		this.tokenizer = new Tokenizer(in);
		next();
	}

	// program ::= function Eos .
	Program program() throws IOException
	{
		FunNode fun = function(null);
		check(Type.Eos, "end-of-stream");
		return new Program(fun);
	}

	// function ::= "{" optParams optLocals optSequence "}" .
	private FunNode function(Scope scope) throws IOException
	{
		checkAndNext(Type.OpenBrace, "{");
		ArrayList<String> params = optParams();
		ArrayList<String> locals = optLocals();
		ArrayList<String> temps = new ArrayList<String>(params);
		temps.addAll(locals);
		checkDuplicates(temps);
		Node code = optSequence(new Scope(temps, scope));
		checkAndNext(Type.CloseBrace, "}");
		return new FunNode(params, locals, code);
	}

	private static void checkDuplicates(ArrayList<String> temps)
	{
		if (new HashSet<String>(temps).size() < temps.size())
			throw new CompilerException("duplicate params or locals");
	}

	// optParams ::= ( "(" optIds ")" )? .
	private ArrayList<String> optParams() throws IOException
	{
		if (type() != Type.OpenParen)
			return new ArrayList<String>();
		next();
		ArrayList<String> params = optIds();
		checkAndNext(Type.CloseParen, ")");
		return params;
	}

	// optLocals ::= optIds .
	private ArrayList<String> optLocals() throws IOException
	{
		return optIds();
	}

	// optIds::= ( id ( "," id )* )? .
	private ArrayList<String> optIds() throws IOException
	{
		ArrayList<String> ids = new ArrayList<String>();
		if (isInFirstOfId())
			return ids;
		ids.add(id());
		while (type() == Type.Comma)
		{
			next();
			ids.add(id());
		}
		return ids;
	}

	private boolean isInFirstOfId()
	{
		switch (type())
		{
		case Id:
			return true;
		default:
			return false;
		}
	}

	// id ::= Id .
	private String id() throws IOException
	{
		check(Type.Id, "id");
		String id = token().name();
		next();
		return id;
	}

	// optSequence ::= ( "->" sequence )? .
	private Node optSequence(Scope scope) throws IOException
	{
		if (type() != Type.To)
			return NilVal.nil;
		next();
		return sequence(scope);
	}

	// sequence ::= optAssignment ( ";" optAssignment )* .
	private Node sequence(Scope scope) throws IOException
	{
		ArrayList<Node> exprs = new ArrayList<Node>();
		Node expr;
		if ((expr = optAssignment(scope)) != null)
			exprs.add(expr);
		while (type() == Type.Semicolon)
		{
			next();
			if ((expr = optAssignment(scope)) != null)
				exprs.add(expr);
		}
		return exprs.size() == 0 ? NilVal.nil : exprs.size() == 1 ? exprs.get(0) : new SeqNode(new NodeList(exprs));
	}

	// optAssignment := assignment? .
	private Node optAssignment(Scope scope) throws IOException
	{
		return isInFirstOfAssignment() ? assignment(scope) : null;
	}

	// assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment
	// | logicalOr .
	private Node assignment(Scope scope) throws IOException
	{
		if (type() == Type.Id)
		{
			String id = token().name();
			next();
			if (isAssignmentOp())
			{
				scope.checkInScope(id);
				Type type = type();
				next();
				return new SetVarNode(id, combineWithOp(id, type, assignment(scope)));
			}
			this.tokenizer.prev();
		}
		return logicalOr(scope);
	}

	private boolean isInFirstOfAssignment()
	{
		switch (type())
		{
		case Id:
		case Plus:
		case Minus:
		case Not:
		case Num:
		case False:
		case True:
		case Nil:
		case String:
		case OpenBrace:
		case OpenParen:
		case If:
		case IfNot:
		case While:
		case WhileNot:
		case Print:
		case Println:
			return true;
		default:
			return false;
		}
	}

	private static Node combineWithOp(String id, Type type, Node expr)
	{
		switch (type)
		{
		case Becomes:
			return expr;
		case PlusBecomes:
			return new BinaryNode(Type.Plus, new GetVarNode(id), expr);
		case MinusBecomes:
			return new BinaryNode(Type.Minus, new GetVarNode(id), expr);
		case TimesBecomes:
			return new BinaryNode(Type.Times, new GetVarNode(id), expr);
		case DivBecomes:
			return new BinaryNode(Type.Div, new GetVarNode(id), expr);
		case ModBecomes:
			return new BinaryNode(Type.Mod, new GetVarNode(id), expr);
		default:
			throw new AssertionError("unreachable code");
		}
	}

	private boolean isAssignmentOp()
	{
		switch (type())
		{
		case Becomes:
		case PlusBecomes:
		case MinusBecomes:
		case TimesBecomes:
		case DivBecomes:
		case ModBecomes:
			return true;
		default:
			return false;
		}
	}

	// logicalOr ::= logicalAnd ( "||" logicalOr )? .
	private Node logicalOr(Scope scope) throws IOException
	{
		Node expr = logicalAnd(scope);
		if (type() != Type.LogOr)
			return expr;
		next();
		return new IfNode(Type.IfNot, expr, logicalAnd(scope), BoolVal.True);
	}

	// logicalAnd ::= equality ( "&&" logicalAnd )? .
	private Node logicalAnd(Scope scope) throws IOException
	{
		Node expr = equality(scope);
		if (type() != Type.LogAnd)
			return expr;
		next();
		return new IfNode(Type.If, expr, logicalAnd(scope), BoolVal.False);
	}

	// equality ::= comparison ( ( "==" | "!=" ) comparison )? .
	private Node equality(Scope scope) throws IOException
	{
		Node expr = comparison(scope);
		if (!isEqualityOp())
			return expr;
		Type oper = type();
		next();
		return new BinaryNode(oper, expr, comparison(scope));
	}

	private boolean isEqualityOp()
	{
		switch (type())
		{
		case Eq:
		case Ne:
			return true;
		default:
			return false;
		}
	}

	// comparison ::= add ( ( "<" | "<=" | ">" | ">=" ) add )? .
	private Node comparison(Scope scope) throws IOException
	{
		Node expr = add(scope);
		if (!isComparisonOp())
			return expr;
		Type oper = type();
		next();
		return new BinaryNode(oper, expr, add(scope));
	}

	private boolean isComparisonOp()
	{
		switch (type())
		{
		case Lt:
		case Le:
		case Gt:
		case Ge:
			return true;
		default:
			return false;
		}
	}

	/*
	 * An imperative way to build a left-leaning AST from a production with
	 * repetition.
	 */
	// add ::= mult ( ( "+" | "-" ) mult )* .
	private Node add(Scope scope) throws IOException
	{
		Node left = mult(scope);
		while (isAddOp())
		{
			Type oper = type();
			next();
			left = new BinaryNode(oper, left, mult(scope));
		}
		return left;
	}

	// /*
	// * Alternative, more functional way to ensure a left-leaning AST;
	// * rewrite left-recursive productions to right-recursive ones.
	// * In essence, when eliminating the tail calls, one ends up
	// * with code similar to the iterative solution above.
	// */
	// private Node add(Scope scope) throws IOException {
	// return addRest(scope, mult(scope));
	// }
	//
	// private Node addRest(Scope scope, Node left) throws IOException {
	// if (!isAddOp())
	// return left;
	// Type type = type();
	// next();
	// return addRest(scope, new BinaryNode(type, left, mult(scope)));
	// }

	private boolean isAddOp()
	{
		switch (type())
		{
		case Plus:
		case Minus:
			return true;
		default:
			return false;
		}
	}

	// mult ::= unary ( ( "*" | "/" | "%" ) unary )* .
	private Node mult(Scope scope) throws IOException
	{
		Node left = unary(scope);
		while (isMultOp())
		{
			Type oper = type();
			next();
			left = new BinaryNode(oper, left, unary(scope));
		}
		return left;
	}

	private boolean isMultOp()
	{
		switch (type())
		{
		case Times:
		case Div:
		case Mod:
			return true;
		default:
			return false;
		}
	}

	// unary ::= ( "+" | "-" | "!" ) unary
	// | postfix .
	private Node unary(Scope scope) throws IOException
	{
		if (!isUnaryOp())
			return postfix(scope);
		Type oper = type();
		next();
		return new UnaryNode(oper, unary(scope));
	}

	private boolean isUnaryOp()
	{
		switch (type())
		{
		case Plus:
		case Minus:
		case Not:
			return true;
		default:
			return false;
		}
	}

	// postfix ::= primary args* .
	private Node postfix(Scope scope) throws IOException
	{
		Node expr = primary(scope);
		while (type() == Type.OpenParen)
			expr = new InvokeNode(expr, args(scope));
		return expr;
	}

	// args ::= "(" ( sequence ( "," sequence )* )? ")" .
	private NodeList args(Scope scope) throws IOException
	{
		checkAndNext(Type.OpenParen, "(");
		ArrayList<Node> args = new ArrayList<Node>();
		if (type() != Type.CloseParen)
		{
			args.add(sequence(scope));
			while (type() == Type.Comma)
			{
				next();
				args.add(sequence(scope));
			}
		}
		checkAndNext(Type.CloseParen, ")");
		return new NodeList(args);
	}

	// primary ::= num | bool | nil | string
	// | getId
	// | function
	// | subsequence
	// | cond
	// | loop
	// | print .
	private Node primary(Scope scope) throws IOException
	{
		switch (type())
		{
		case Num:
			return num();
		case False:
		case True:
			return bool();
		case Nil:
			return nil();
		case String:
			return string();
		case Id:
			return getId(scope);
		case OpenBrace:
			return function(scope);
		case OpenParen:
			return subsequence(scope);
		case If:
		case IfNot:
			return cond(scope);
		case While:
		case WhileNot:
			return loop(scope);
		case Print:
		case Println:
			return print(scope);
		default:
			throw new CompilerException("invalid expression (" + token() + ")");
		}
	}

	// print ::= ( "print" | "println" ) args .
	private PrintNode print(Scope scope) throws IOException
	{
		check(Type.Print, Type.Println, "print / println");
		Type type = type();
		next();
		return new PrintNode(type, args(scope));
	}

	// loop ::= ( "while" | "whilenot" ) sequence ( "do" sequence )? "od" .
	private WhileNode loop(Scope scope) throws IOException
	{
		check(Type.While, Type.WhileNot, "while / whilenot");
		Type type = type();
		next();
		Node condExpr = sequence(scope);
		Node doExpr = null;
		if (type() == Type.Do)
		{
			next();
			doExpr = sequence(scope);
		} else
			doExpr = NilVal.nil;
		checkAndNext(Type.Od, "od");
		return new WhileNode(type, condExpr, doExpr);
	}

	// cond ::= ( "if" | "ifnot" ) sequence "then" sequence ( "else" sequence )?
	// "fi" .
	private IfNode cond(Scope scope) throws IOException
	{
		check(Type.If, Type.IfNot, "if / ifnot");
		Type type = type();
		next();
		Node condExpr = sequence(scope);
		checkAndNext(Type.Then, "then");
		Node thenExpr = sequence(scope);
		Node elseExpr;
		if (type() == Type.Else)
		{
			next();
			elseExpr = sequence(scope);
		} else
			elseExpr = NilVal.nil;
		checkAndNext(Type.Fi, "fi");
		return new IfNode(type, condExpr, thenExpr, elseExpr);
	}

	// getId ::= Id .
	private GetVarNode getId(Scope scope) throws IOException
	{
		check(Type.Id, "id");
		String id = token().name();
		scope.checkInScope(id);
		next();
		return new GetVarNode(id);
	}

	// num ::= Num .
	private NumVal num() throws IOException
	{
		check(Type.Num, "num");
		BigDecimal num = token().num();
		next();
		return new NumVal(num);
	}

	// bool ::= True | False .
	private BoolVal bool() throws IOException
	{
		check(Type.False, Type.True, "false / true");
		Type bool = type();
		next();
		return BoolVal.valueOf(bool == Type.True);
	}

	// nil ::= Nil .
	private NilVal nil() throws IOException
	{
		check(Type.Nil, "nil");
		next();
		return NilVal.nil;
	}

	// string ::= String .
	private StringVal string() throws IOException
	{
		check(Type.String, "string");
		String s = token().name();
		next();
		return new StringVal(s);
	}

	// subsequence ::= "(" sequence ")" .
	private Node subsequence(Scope scope) throws IOException
	{
		checkAndNext(Type.OpenParen, "(");
		Node expr = sequence(scope);
		checkAndNext(Type.CloseParen, ")");
		return expr;
	}

	private Type type()
	{
		return token().type();
	}

	private void checkAndNext(Type expected, String message) throws IOException
	{
		check(expected, message);
		next();
	}

	private void check(Type expected, String message)
	{
		if (type() != expected)
			throwUnexpected(message);
	}

	private void check(Type expected1, Type expected2, String message)
	{
		if (type() != expected1 && type() != expected2)
			throwUnexpected(message);
	}

	private void throwUnexpected(String message)
	{
		throw new CompilerException(message + " expected [current token: " + token() + "]");
	}

	private Token token()
	{
		return this.tokenizer.token();
	}

	private void next() throws IOException
	{
		this.tokenizer.next();
	}

}