/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class TokenizerException extends RuntimeException {

    TokenizerException(String message) {
        super("tokenizer: " + message);
    }
    
}