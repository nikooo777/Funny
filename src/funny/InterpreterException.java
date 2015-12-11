/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class InterpreterException extends RuntimeException {

    InterpreterException(String message) {
        super("interpreter: " + message);
    }

}