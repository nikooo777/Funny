/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class TokenizerException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4223827880503356045L;

	TokenizerException(String message)
	{
		super("tokenizer: " + message);
	}
}