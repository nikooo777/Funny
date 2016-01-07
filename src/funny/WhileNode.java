/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class WhileNode extends Node
{

	private final Node condNode;
	private final Node doNode;
	private Type type;

	WhileNode(Type type, Node condNode, Node doNode)
	{
		this.type = type;
		this.condNode = condNode;
		this.doNode = doNode;
	}

	@Override
	Val eval(Env env)
	{
		Val val = NilVal.nil;
		boolean invert = type == Type.WhileNot;
		while (!condNode.eval(env).checkBoolean().bool() ^ invert)
			val = doNode.eval(env);
		return val;
	}
}