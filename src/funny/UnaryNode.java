/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class UnaryNode extends Node
{
	private final Type oper;
	private final Node node;

	UnaryNode(Type oper, Node node)
	{
		this.oper = oper;
		this.node = node;
	}

	@Override
	Val eval(Env env)
	{
		switch (this.oper)
		{
		case Not:
			return this.node.eval(env).not();
		case Minus:
			return this.node.eval(env).minus();
		default:
			throw new InterpreterException("Unimplemented unary operation");
		}
	}
}