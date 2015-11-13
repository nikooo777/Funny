package ch.supsi.compinterp.funny;

import java.math.BigDecimal;

class Token
{
	enum Type
	{
		Eos,
		AssignEquals,
		PlusEquas,
		MinusEquals,
		MultiplicationEquas,
		DivisionEquals,
		ModuloEquals,
		AndAnd,
		OrOr,
		EqualsEquals,
		NotEquals,
		Lt,
		Gt,
		LtEquals,
		GtEquals,
		Plus,
		Minus,
		Multiplication,
		Division,
		Modulo,
		Not,
		Comma,
		Num,
		Bool,
		Nil,
		String,
		Id,
		If,
		IfNot,
		Then,
		Else,
		Fi,
		While,
		WhileNot,
		Do,
		od,
		Print,
		PrintLn,
		Star,
		OpenParen,
		CloseParen,
		OpenBraces,
		CloseBraces,
		SemiColon,
		To,
		False,
		True,

		Unknown;
	}

	public static Token simple(Type unknown)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private final Type type;

	private final BigDecimal num;

	Token(Type type)
	{
		this(type, new BigDecimal(-1));
	}

	Token(Type type, BigDecimal num)
	{
		this.type = type;
		this.num = num;
	}

	BigDecimal num()
	{
		return num;
	}

	@Override
	public String toString()
	{
		switch (type())
		{
		case Num:
			return type() + " [" + num() + "]";
		default:
			return type().toString();
		}
	}

	Type type()
	{
		return type;
	}
}
