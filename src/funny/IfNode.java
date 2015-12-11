/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class IfNode extends Node
{

	private final Node condNode;
	private final Node thenNode;
	private final Node elseNode;

	IfNode(Type type, Node condNode, Node thenNode, Node elseNode)
	{
		this.condNode = condNode;
		this.thenNode = thenNode;
		this.elseNode = elseNode;
	}

	@Override
	Val eval(Env env)
	{
		throw new InterpreterException("not yet implemented");
	}

}