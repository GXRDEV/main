package com.tspeiz.modules.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloatArith {

	public FloatArith() {
		// TODO Auto-generated constructor stub
	}

	public static float add_simple(float v1, float v2) {

		BigDecimal b1 = new BigDecimal(Float.toString(v1));

		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		return b1.add(b2).floatValue();

	}

	public static float sub_simple(float v1, float v2) {

		BigDecimal b1 = new BigDecimal(Float.toString(v1));

		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		return b1.subtract(b2).floatValue();

	}

	public static float add(float v1, float v2, int scale,
			RoundingMode roundingMode) {

		BigDecimal b1 = new BigDecimal(Float.toString(v1));

		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		BigDecimal bd = b1.add(b2);

		bd = bd.setScale(scale, roundingMode);

		return bd.floatValue();

	}

	public static float sub(float v1, float v2, int scale,
			RoundingMode roundingMode) {

		BigDecimal b1 = new BigDecimal(Float.toString(v1));

		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		BigDecimal bd = b1.subtract(b2);

		bd = bd.setScale(scale, roundingMode);

		return bd.floatValue();

	}

}
