package ch.supsi.compinterp.funny;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import ch.supsi.compinterp.funny.Token.Type;

/*
 * program ::= function Eos .
 * 
 * function ::= "{" optParams optLocals optSequence "}" .
 * optParams ::= ( "(" optIds ")" )? .
 * optLocals ::= optIds .
 * optSequence ::= ( "->" sequence )? .
 * optIds::= ( id ( "," id )* )? .
 * id ::= Id .
 * 
 * sequence ::= optAssignment ( ";" optAssignment )* .
 * optAssignment := assignment? .
 * assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment
 * | logicalOr .
 *
 * logicalOr ::= logicalAnd ( "||" logicalOr )? .
 * logicalAnd ::= equality ( "&&" logicalAnd )? .
 * equality ::= comparison ( ( "==" | "!=" ) comparison )? .
 * comparison ::= add ( ( "<" | "<=" | ">" | ">=" ) add )? .
 * add ::= mult ( ( "+" | "-" ) mult )* .
 * mult ::= unary ( ( "*" | "/" | "%" ) unary )* .
 * unary ::= ( "+" | "-" | "!" ) unary
 * | postfix .
 *
 * postfix ::= primary args* .
 * args ::= "(" ( sequence ( "," sequence )* )? ")" .
 * primary ::= num | bool | nil | string
 * | getId
 * | function
 * | subsequence
 * | cond
 * | loop
 * | print .
 *
 * num ::= Num .
 * bool ::= True | False .
 * nil ::= Nil .
 * string ::= String .
 * getId ::= Id .
 * subsequence ::= "(" sequence ")" .
 * cond ::= ( "if" | "ifnot" ) sequence "then" sequence ( "else" sequence )? "fi" .
 * loop ::= ( "while" | "whilenot" ) sequence ( "do" sequence )? "od" .
 * print ::= ( "print" | "println" ) args .
 */
public class Parser
{
	// fake tokenizer
	private MockupTokenizer tokenizer;

	// current token in process
	private Token token;

	// constructor
	/**
	 * Given an input reader, a tokenizer is instantiated and the first token element is polled
	 * 
	 * @param in_reader
	 * @throws IOException
	 */
	public Parser(Reader in) throws IOException
	{
		// tokenizer setup
		tokenizer = new MockupTokenizer(in);

		// first token polling
		next();
	}

	/**
	 * checks that the current type is the expected type
	 * 
	 * @param type
	 * @param expected
	 */
	private void check(Type type, String expected)
	{
		if (type() != type)
			throw new ParseException("Found: " + type + " expected: " + expected);
	}

	/**
	 * performs a check, if passed then next is also called
	 * 
	 * @param type
	 * @param expected
	 * @throws IOException
	 */
	private void checkAndNext(Token.Type type, String expected) throws IOException
	{
		check(type, expected);
		next();
	}

	/*
	 * private void checkDuplicates(ArrayList<String> temps)
	 * {
	 * 
	 * }
	 */

	/**
	 * function ::= "{" optParams optLocals optSequence "}" .
	 * @return node root
	 * @throws IOException
	 * @throws
	 */
	private Node function(/* Scope scope */) throws IOException
	{
		// required open brace
		checkAndNext(Type.OpenBrace, "{");
		// lists for optional parameters, locals, temporary list for checks
		ArrayList<String> params = optParams();
		ArrayList<String> locals = optLocals();
		ArrayList<String> temps = new ArrayList<>(params);
		temps.addAll(locals);
		// check if there are duplicates between params and locals
		// checkDuplicates(temps);
		// ask for optional sequence which is also a Node
		Node code = optSequence(/* new Scope(temps,scope) */);
		// require closing brace
		checkAndNext(Type.CloseBraces, "}");
		return new FunNode(params, locals, code);
	}

	/**
	 * This method returns a list of optional parameters
	 * optParams ::= ( "(" optIds ")" )? .
	 * 
	 * @return optionalParameters
	 * @throws IOException
	 */
	private ArrayList<String> optParams() throws IOException
	{
		// check if we have the opening parenthesis
		if (type() == Type.OpenParen)
		{
			// update the token
			next();
			// get the optional ids
			ArrayList<String> optional_ids = optIds();
			// check for the closing parenthesis
			checkAndNext(Type.CloseParen, ")");
			return optional_ids;
		}
		// if there are no optional parameters then we return an empty arraylist
		return new ArrayList<>();
	}

	/**
	 * returns a list of optional ids
	 * optIds::= ( id ( "," id )* )? .
	 * 
	 * @return optionalIds
	 * @throws IOException
	 */
	private ArrayList<String> optIds() throws IOException
	{
		// optional ID - if not present then return empty list
		if (type() != Type.Id)
			return new ArrayList<>();

		// init arraylist for ids
		ArrayList<String> ids = new ArrayList<>();

		// we certainly have an ID now to parse, therefore parse it and add it to the list
		ids.add(id());

		// update token
		next();

		// add any further id after comma
		while (type() == Type.Comma)
		{
			next();
			check(Type.Id, "Id");
			ids.add(id());
			next();
		}
		return ids;
	}

	/**
	 * id ::= Id .
	 * 
	 * @return id
	 * @throws IOException
	 */
	private String id() throws IOException
	{
		check(Type.Id, "Id");
		String id = token.name();
		next();
		return id;
	}

	/**
	 * optLocals ::= optIds .
	 * 
	 * @return optionalIds
	 * @throws IOException
	 */
	private ArrayList<String> optLocals() throws IOException
	{
		return optIds();
	}

	/**
	 * optSequence ::= ( "->" sequence )? .
	 * 
	 * @return NodeSequence
	 * @throws IOException
	 */
	private Node optSequence() throws IOException
	{
		if (type() != Type.To)
			return null;

		next();
		return sequence();

	}

	/**
	 * sequence ::= optAssignment ( ";" optAssignment )* .
	 * 
	 * @return
	 * @throws IOException
	 */
	private Node sequence() throws IOException
	{
		ArrayList<Node> expressions = new ArrayList<>();
		Node expression;
		if ((expression = optAssignment()) != null)
			expressions.add(expression);
		while (type() == Type.SemiColon)
		{
			next();
			if ((expression = optAssignment()) != null)
				expressions.add(expression);
		}
		// either return Null if empty, SequenceNode if just one was found or an arraylist of nodes if more were found
		return (expressions.size() == 0) ? null : (expressions.size() == 1) ? expressions.get(0) : new SequenceNode(expressions);
	}

	/**
	 * optAssignment := assignment? .
	 * 
	 * @return
	 */
	private Node optAssignment()
	{
		return isInFirstOfAssignment() ? assignment() : null;
	}

	/**
	 * assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment | logicalOr .
	 * 
	 * @return
	 * @throws IOException
	 */
	private Node assignment() throws IOException
	{
		if (type() == Type.Id)
		{
			String id = token.name();
			next();
			if (isAssignmentOperation())
			{
				Type assignmentOperation = type();
				next();
				return new AssignmentNode(id, assignmentOperation, assignment());
			}
			tokenizer.previous();
		}
		// SONO FERMO QUA --------> | logicalOr .
		return logicalOr();
	}

	private boolean isAssignmentOperation()
	{
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isInFirstOfAssignment()
	{
		switch (type())
		{
		case Plus:
		case Minus:
		case Not:
		case Num:
		case True:
		case False:
		case Nil:
		case String:
		case Id:
		case OpenBrace:
		case OpenParen:
		case If:
		case IfNot:
		case While:
		case WhileNot:
		case Print:
		case PrintLn:
			return true;

		default:
			return false;
		}
	}

	/**
	 * this method updates the current token to the next one to process
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	private void next() throws IOException
	{
		token = tokenizer.next();
	}

	/**
	 * this method does the whole parsing
	 * 
	 * @return node
	 * @throws IOException
	 */
	public Node parse() throws IOException
	{
		// as per grammar a fucntion is expected first
		Node node = function();

		// at the end, and EOS is expected
		check(Type.Eos, "end-of-stream");

		// return the program
		return node;
	}

	/**
	 * returns the type of the current token
	 * 
	 * @return type
	 */
	private Type type()
	{
		return token.type();
	}
}
