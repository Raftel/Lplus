package com.lplus.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class LplusUtil {

	public static int dpToPx(DisplayMetrics dm, int dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, dm);
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

	public static boolean getNetworkState(Context context) {
		ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect != null) {
			return (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED 
					|| connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
					|| connect.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH).getState() == NetworkInfo.State.CONNECTED);
		}
		
		return false;
	}
}
