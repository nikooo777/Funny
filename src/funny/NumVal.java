/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.math.BigDecimal;

class NumVal extends Val
{
	private final BigDecimal num;

	NumVal(BigDecimal num)
	{
		this.num = num;
	}

	@Override
	NumVal checkNum()
	{
		return this;
	}

	BoolVal eq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) == 0);
	}

	BoolVal neq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) != 0);
	}

	BoolVal lt(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) < 0);
	}

	BoolVal gt(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) > 0);
	}

	BoolVal leq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) <= 0);
	}

	BoolVal geq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) >= 0);
	}

}