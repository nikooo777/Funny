/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

class NumVal extends Val
{
	private final BigDecimal num;
	static MathContext mc = new MathContext(100, RoundingMode.HALF_EVEN);

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
	Val times(Val val)
	{
		return new NumVal(this.num.multiply(val.checkNum().num));
	}

	@Override
	Val divide(Val val)
	{
		try
		{
			return new NumVal(this.num.divide(val.checkNum().num));
		} catch (Exception e)
		{
			return new NumVal(this.num.divide(val.checkNum().num, mc));
		}
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
	NumVal mod(Val val)
	{
		return new NumVal(this.num.divideAndRemainder(val.checkNum().num)[1]);
	};

	@Override
	NumVal plus()
	{
		return new NumVal(this.num.plus());
	};

	@Override
	NumVal minus()
	{
		return new NumVal(this.num.negate());
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
		return BoolVal.valueOf(this.num.compareTo(val.checkNum().num) >= 0);
	}

	@Override
	public String toString()
	{
		return this.num.toString();
	}
}