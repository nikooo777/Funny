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
		case Minus:
			return this.left.eval(env).minus(this.right.eval(env));
		case Times:
			return this.left.eval(env).times(this.right.eval(env));
		case Div:
			return this.left.eval(env).divide(this.right.eval(env));
		case Gt:
			return this.left.eval(env).gt(this.right.eval(env));
		case Lt:
			return this.left.eval(env).lt(this.right.eval(env));
		case Mod:
			return this.left.eval(env).mod(this.right.eval(env));
		case Ge:
			return this.left.eval(env).geq(this.right.eval(env));
		case Le:
			return this.left.eval(env).leq(this.right.eval(env));
		case Ne:
			return this.left.eval(env).neq(this.right.eval(env));
		case Eq:
			return this.left.eval(env).eq(this.right.eval(env));

		default:
			throw new InterpreterException("Unimplemented binary operation");
		}
	}

}