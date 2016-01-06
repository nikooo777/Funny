/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;
import java.util.HashMap;

class Frame
{
	HashMap<String, Val> bindings = new HashMap<>();

	Frame(ArrayList<String> params, ArrayList<String> locals, ArrayList<Val> argVals)
	{
		checkArity(params, argVals);
		for (int i = 0; i < params.size(); i++)
		{
			this.bindings.put(params.get(i), argVals.get(i));
		}
		for (int i = 0; i < locals.size(); i++)
		{
			this.bindings.put(locals.get(i), NilVal.nil);
		}

	}

	private void checkArity(ArrayList<String> params, ArrayList<Val> argVals)
	{
		if (params.size() != argVals.size())
			throw new InterpreterException("not the same size");
	}

	public boolean contains(String id)
	{
		return this.bindings.containsKey(id);
	}

	Val getVal(String key)
	{
		return this.bindings.get(key);
	}

	public Val setVal(String id, Val val)
	{
		this.bindings.put(id, val);
		return val;
	}

}