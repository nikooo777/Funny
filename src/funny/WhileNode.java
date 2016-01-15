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
	private boolean invert;

	WhileNode(Type type, Node condNode, Node doNode)
	{
		this.invert = type == Type.WhileNot;
		this.condNode = condNode;
		this.doNode = doNode;
	}

	@Override
	Val eval(Env env)
	{
		Val val = NilVal.nil;
		while (this.condNode.eval(env).checkBoolean().bool() ^ this.invert)
			val = this.doNode.eval(env);
		return val;
	}
}