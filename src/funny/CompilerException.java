/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class CompilerException extends RuntimeException
{

	CompilerException(String message)
	{
		super("compiler: " + message);
	}

}