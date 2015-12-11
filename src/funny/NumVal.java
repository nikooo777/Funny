/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.math.BigDecimal;

class NumVal extends Val {

    private final BigDecimal num;

    NumVal(BigDecimal num) {
        this.num = num;
    }

}