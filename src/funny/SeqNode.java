/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class SeqNode extends Node {

    private final NodeList nodes;

    SeqNode(NodeList nodes) {
        this.nodes = nodes;
    }

    @Override
    Val eval(Env env) {
        throw new InterpreterException("not yet implemented");
    }

}