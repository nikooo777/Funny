/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class Env
{

	private final Frame frame;
	private final Env enclosing;

	Env(Frame frame, Env enclosing)
	{
		this.frame = frame;
		this.enclosing = enclosing;
	}

	public Val getVal(String id)
	{
		return (this.frame.contains(id)) ? this.frame.getVal(id) : this.enclosing.getVal(id);
	}

	public Val setVal(String id, Val val)
	{
		return (this.frame.contains(id)) ? this.frame.setVal(id, val) : this.enclosing.setVal(id, val);
	}

}