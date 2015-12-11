/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class NilVal extends Val
{

	static final NilVal nil = new NilVal();

	private NilVal()
	{
	}

	@Override
	public String toString()
	{
		return "Nil";
	}
}