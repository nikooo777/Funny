/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;

class Scope
{

	private final ArrayList<String> temps;
	private final Scope enclosing;

	Scope(ArrayList<String> temps, Scope scope)
	{
		this.temps = temps;
		this.enclosing = scope;
	}

	Scope enclosing()
	{
		return this.enclosing;
	}

	void checkInScope(String id)
	{
		if (!isInScope(id))
			throw new CompilerException("id " + id + " is not in scope");
	}

	private boolean isInScope(String id)
	{
		return this.temps.contains(id) ? true : this.enclosing != null ? this.enclosing.isInScope(id) : false;
	}

}