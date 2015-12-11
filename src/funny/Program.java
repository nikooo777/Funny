/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;

class Program {

    private final FunNode funNode;

    Program(FunNode funNode) {
        this.funNode = funNode;
    }

    Val eval() {
        return new InvokeNode(funNode, new NodeList(new ArrayList<Node>(0))).eval(null);
    }
    
}