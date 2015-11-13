package ch.supsi.compinterp.funny;

import java.io.IOException;

import ch.supsi.compinterp.funny.Token.Type;

public class Tokenizer
{
	private Token prevToken, token;
	private boolean stepBack;

	private Token id()
	{
		// TODO Auto-generated method stub
		return null;
	}

	Token next() throws IOException
	{
		if (stepBack)
		{
			stepBack = false;
			return token;
		}
		prevToken = token;

		skipWhites();
		if ((token = id()) != null)
			return token;
		if ((token = num()) != null)
			return token;
		if ((token = operatorOrDelimiter()) != null)
			return token;
		if ((token = string()) != null)
			return token;

		return token = Token.simple(Type.Unknown);
	}

	private Token num()
	{
		// TODO Auto-generated method stub
		return null;
	}

	private Token operatorOrDelimiter()
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void skipWhites()
	{
		// TODO Auto-generated method stub

	}

	private Token string()
	{
		// TODO Auto-generated method stub
		return null;
	}

	Token token()
	{
		return stepBack ? prevToken : token;
	}
}
