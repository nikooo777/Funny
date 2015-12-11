/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;

class Scope {

    private final ArrayList<String> temps;
    private final Scope enclosing;

    Scope(ArrayList<String> temps, Scope scope) {
        this.temps = temps;
        this.enclosing = scope;
    }

    Scope enclosing() {
        return enclosing;
    }

    void checkInScope(String id) {
        if (!isInScope(id))
            throw new CompilerException("id " + id + " is not in scope");
    }
    
    private boolean isInScope(String id) {
        return temps.contains(id) ? true : enclosing != null ? enclosing.isInScope(id) : false;
    }
    
}