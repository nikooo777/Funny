/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.HashMap;

class Frame
{
	HashMap<String, Val> bindings = new HashMap<>();

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