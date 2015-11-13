package ch.supsi.compinterp.funny;

public class ParseException extends RuntimeException
{
	private static final long serialVersionUID = 275819706826180631L;

	ParseException(String s)
	{
		super(s);
	}
}