/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;

class ClosureVal extends Val
{

	private final Env env;
	private final FunNode funNode;

	ClosureVal(Env env, FunNode funNode)
	{
		this.env = env;
		this.funNode = funNode;
	}

	Val apply(ArrayList<Val> argVals)
	{
		return this.funNode.code().eval(new Env(new Frame(this.funNode.params(), this.funNode.locals(), argVals), this.env));
	}

	@Override
	ClosureVal checkClosure()
	{
		return this;
	}
}