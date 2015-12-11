/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class PrintNode extends Node {

    private final Type type;
    private final NodeList args;

    PrintNode(Type type, NodeList args) {
        this.type = type;
        this.args = args;
    }

    @Override
    Val eval(Env env) {
        throw new InterpreterException("not yet implemented");
    }

}