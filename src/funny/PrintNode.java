/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class PrintNode extends Node
{

	private final Type type;
	private final NodeList args;

	PrintNode(Type type, NodeList args)
	{
		this.type = type;
		this.args = args;
	}

	@Override
	Val eval(Env env)
	{
		Val val = NilVal.nil;
		for (Node node : this.args.asArrayList())
		{
			val = node.eval(env);
			System.out.print(val);
		}
		if (type == Type.Println)
		{
			System.out.println();
		}
		return val;
	}
}