/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class StringVal extends Val
{

	private final String string;

	StringVal(String string)
	{
		this.string = string;
	}

	@Override
	boolean isString()
	{
		return true;
	}

	@Override
	public Val plus(Val val)
	{
		return new StringVal(this + val.toString());
	}

	@Override
	public String toString()
	{
		return this.string;
	}
}