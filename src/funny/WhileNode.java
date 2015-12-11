/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import funny.Token.Type;

class WhileNode extends Node {

    private final Node condNode;
    private final Node doNode;

    WhileNode(Type type, Node condNode, Node doNode) {
        this.condNode = condNode;
        this.doNode = doNode;
    }

    @Override
    Val eval(Env env) {
        throw new InterpreterException("not yet implemented");
    }

}