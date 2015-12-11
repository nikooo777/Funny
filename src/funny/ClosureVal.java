/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class ClosureVal extends Val {

    private final Env env;
    private final FunNode funNode;

    ClosureVal(Env env, FunNode funNode) {
        this.env = env;
        this.funNode = funNode;
    }

}