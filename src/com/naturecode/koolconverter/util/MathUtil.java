package com.naturecode.koolconverter.util;

import java.math.BigDecimal;

public class MathUtil {
	static public double getDRound(double d, int decimal_place) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(decimal_place, BigDecimal.ROUND_HALF_EVEN);
		d = bd.doubleValue();
		return d;
	}
}