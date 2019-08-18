package com.mulyadime.reveluv.utilities;

import java.util.Formatter;

public class AppUtil {

	public static final String formatToString(String format, Object... args) {
		return new Formatter().format(format, args).toString();
	}

}
