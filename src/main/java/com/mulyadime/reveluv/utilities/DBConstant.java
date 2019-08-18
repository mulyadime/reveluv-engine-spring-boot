package com.mulyadime.reveluv.utilities;

import java.sql.Types;

public class DBConstant {
	
	public static class CriteriaType {
		public static final String LONG = "LONG";
		public static final String STRING = "STRING";
		public static final String INTEGER = "INTEGER";
		
	}
	
	public static class OperationType {
		public static final String LONG = "LONG";
		public static final String DBL_PERSEN = "DBL_PERSEN";
		public static final String NO_PERSEN = "NO_PERSEN";
		
	}

	public static class SQLType {
		public static final int LONG = Types.NUMERIC;
		public static final int INTEGER = Types.INTEGER;
		public static final int STRING = Types.VARCHAR;
		public static final int BOOLEAN = Types.BOOLEAN;
		
	}
	
}
