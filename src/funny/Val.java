/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.math.BigDecimal;

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

	BoolVal checkBoolean()
	{
		throw new InterpreterException("This is not a boolean");
	}

	NumVal checkNum()
	{
		throw new InterpreterException("This is not a number");
	}

}