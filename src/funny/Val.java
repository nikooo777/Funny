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

	Val plus(Val val)
	{
		if (val.isString())
			return new StringVal(toString() + val);
		throw new InterpreterException("Plus can't be applied to this val");
	}

	Val plus()
	{
		throw new InterpreterException("Plus can't be applied to this val");
	}

	Val minus(Val eval)
	{
		throw new InterpreterException("Minus can't be applied to this val");
	}

	Val minus()
	{
		throw new InterpreterException("Minus can't be applied to this val");
	}

	BoolVal checkBoolean()
	{
		throw new InterpreterException("This is not a boolean");
	}

	NumVal checkNum()
	{
		throw new InterpreterException("This is not a number");
	}

	BoolVal eq(Val eval)
	{
		throw new InterpreterException("This is not a boolean");
	}

	BoolVal neq(Val val)
	{
		throw new InterpreterException("This is not a boolean");
	}

	BoolVal lt(Val val)
	{
		throw new InterpreterException("This is not a boolean");
	}

	BoolVal gt(Val val)
	{
		throw new InterpreterException("This is not a boolean");
	}

	BoolVal leq(Val val)
	{
		throw new InterpreterException("This is not a boolean");
	}

	BoolVal geq(Val val)
	{
		throw new InterpreterException("This is not a boolean");
	}

	BoolVal not()
	{
		throw new InterpreterException("This is not a boolean");
	}

	ClosureVal checkClosure()
	{
		throw new InterpreterException("This is not a a closure");
	}
}