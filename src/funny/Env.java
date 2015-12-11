/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

class Env {

    private final Frame frame;
    private final Env enclosing;

    Env(Frame frame, Env enclosing) {
        this.frame = frame;
        this.enclosing = enclosing;
    }

}