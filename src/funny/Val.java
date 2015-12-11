/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

abstract class Val extends Node {

    @Override
    Val eval(Env env) {
        throw new InterpreterException("not yet implemented");
    }

}