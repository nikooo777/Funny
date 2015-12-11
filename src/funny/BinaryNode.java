/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class BinaryNode extends Node
{

	private final Type oper;
	private final Node left;
	private final Node right;

	BinaryNode(Type oper, Node left, Node right)
	{
		this.oper = oper;
		this.left = left;
		this.right = right;
	}

	@Override
	Val eval(Env env)
	{
		switch (this.oper)
		{
		case Plus:
			return this.left.eval(env).plus(this.right.eval(env));
		default:
			throw new InterpreterException("Unimplemented");
		}
	}

}