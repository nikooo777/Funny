/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class BoolVal extends Val {

    static final BoolVal False = new BoolVal(false);
    static final BoolVal True = new BoolVal(true);

    private final boolean bool;

    private BoolVal(boolean bool) {
        this.bool = bool;
    }

    static BoolVal valueOf(boolean b) {
        return b ? True : False;
    }
    
}