/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class InterpreterException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7172853548521564776L;

	InterpreterException(String message)
	{
		super("interpreter: " + message);
	}
}