/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class InvokeNode extends Node
{

	private final Node node;
	private final NodeList args;

	InvokeNode(Node node, NodeList args)
	{
		this.node = node;
		this.args = args;
	}

	@Override
	Val eval(Env env)
	{
		throw new InterpreterException("not yet implemented");
	}

}