/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

abstract class Val extends Node
{
	@Override
	Val eval(Env env)
	{
		return this;
	}

	boolean isString()
	{
		return false;
	}

	public Val plus(Val val)
	{
		if (val.isString())
			return new StringVal(toString() + val);
		throw new InterpreterException("Plus can't be applied to this val");
	}

}