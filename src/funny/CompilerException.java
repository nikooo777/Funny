/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class CompilerException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7227062944733987652L;

	CompilerException(String message)
	{
		super("compiler: " + message);
	}
}