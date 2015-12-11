/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class SetVarNode extends Node {

    private final String id;
    private final Node node;

    SetVarNode(String id, Node node) {
        this.id = id;
        this.node = node;
    }

    @Override
    Val eval(Env env) {
        throw new InterpreterException("not yet implemented");
    }

}