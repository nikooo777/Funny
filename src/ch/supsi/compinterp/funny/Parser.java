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

	private void checkDuplicates(ArrayList<String> temps)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * function ::= "{" optParams optLocals optSequence "}" .
	 * @return node root
	 * @throws IOException
	 * @throws
	 */
	private Node function(/* Scope scope */) throws IOException
	{
		// required open brace
		checkAndNext(Type.OpenBraces, "{");
		// lists for optional parameters, locals, temporary list for checks
		ArrayList<String> params = optParams();
		ArrayList<String> locals = optLocals();
		ArrayList<String> temps = new ArrayList<>(params);
		temps.addAll(locals);
		// check if there are duplicates between params and locals
		checkDuplicates(temps);
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
	 */
	private ArrayList<String> optIds()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * optLocals ::= optIds .
	 * 
	 * @return optionalIds
	 */
	private ArrayList<String> optLocals()
	{
		return optIds();
	}

	private Node optSequence()
	{
		// TODO Auto-generated method stub
		return null;
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
