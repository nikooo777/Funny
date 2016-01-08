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

	@Override
	NumVal plus(Val val)
	{
		return new NumVal(this.num.add(val.checkNum().num));
	};

	@Override
	NumVal minus(Val val)
	{
		return new NumVal(this.num.subtract(val.checkNum().num));
	};

	@Override
	NumVal plus()
	{
		return new NumVal(this.num.add(new BigDecimal(1)));
	};

	@Override
	NumVal minus()
	{
		return new NumVal(this.num.subtract(new BigDecimal(1)));
	};

	@Override
	BoolVal eq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(val.checkNum().num) == 0);
	}

	@Override
	BoolVal neq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(val.checkNum().num) != 0);
	}

	@Override
	BoolVal lt(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(val.checkNum().num) < 0);
	}

	@Override
	BoolVal gt(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(val.checkNum().num) > 0);
	}

	@Override
	BoolVal leq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(val.checkNum().num) <= 0);
	}

	@Override
	BoolVal geq(Val val)
	{
		return BoolVal.valueOf(this.num.compareTo(checkNum().num) >= 0);
	}

}