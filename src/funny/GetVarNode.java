/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class GetVarNode extends Node
{
	private final String id;

	GetVarNode(String id)
	{
		this.id = id;
	}

	@Override
	Val eval(Env env)
	{
		return env.getVal(this.id);
	}
}