package ch.supsi.compinterp.funny;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockupTokenizer
{
	private final List<Token> tokens = new ArrayList<>();
	private int pos = 0;

	MockupTokenizer(final Reader in) throws IOException
	{
		tokens.add(new Token((Token.Type.OpenBrace)));
		tokens.add(new Token((Token.Type.OpenParen)));
		tokens.add(new Token((Token.Type.Id)));
		tokens.add(new Token((Token.Type.CloseParen)));
		tokens.add(new Token((Token.Type.To)));
		tokens.add(new Token((Token.Type.Id)));
		tokens.add(new Token((Token.Type.AssignEquals)));
		tokens.add(new Token(Token.Type.Num, new BigDecimal(5)));
		tokens.add(new Token((Token.Type.CloseBraces)));
	}

	Token next()
	{
		return tokens.get(pos++);
	}

	Token token()
	{
		return tokens.get(pos - 1);
	}
}
