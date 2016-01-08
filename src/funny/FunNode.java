/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;

class FunNode extends Node
{
	private final ArrayList<String> params;
	private final ArrayList<String> locals;
	private final Node code;

	FunNode(ArrayList<String> params, ArrayList<String> locals, Node code)
	{
		this.params = params;
		this.locals = locals;
		this.code = code;
	}

	@Override
	Val eval(Env env)
	{
		return new ClosureVal(env, this);
	}

	Node code()
	{
		return this.code;
	}

	ArrayList<String> locals()
	{
		return this.locals;
	}

	ArrayList<String> params()
	{
		return this.params;
	}
}