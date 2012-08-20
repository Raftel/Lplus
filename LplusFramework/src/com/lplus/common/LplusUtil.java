package com.lplus.common;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class LplusUtil {

	public static int dpToPx(DisplayMetrics dm, int dip) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm);
	}
	
	public static int getMin(int first, int second) {
		if (first > second) {
			return second;
		} else {
			return first;
		}
	}
	
	public static int getMax(int first, int second) {
		if (first < second) {
			return second;
		} else {
			return first;
		}
	}
}
