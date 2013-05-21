package me.microgeek.xmlconfig.config;

public enum XMLDataType implements DataParser {

	/**
	 * Seems a bit redundant, not easy to modify for Objects like Locations and ItemStacks. I guess you can pass an Object array instead of a String, the Interface {@link DataParser} needs to be changed 
	 */
	
	BOOLEAN(Boolean.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Boolean.parseBoolean(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Boolean parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Boolean.parseBoolean(string);
		}
	},
	BYTE(Byte.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Byte.parseByte(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Byte parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Byte.parseByte(string);
		}
	},
	DOUBLE(Double.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Double.parseDouble(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Double parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Double.parseDouble(string);
		}
	},
	FLOAT(Float.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Float.parseFloat(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Float parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Float.parseFloat(string);
		}
	},
	INTEGER(Integer.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Integer.parseInt(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Integer parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Integer.parseInt(string);
		}
	},
	LONG(Long.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Long.parseLong(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Long parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Long.parseLong(string);
		}
	},
	OBJECT(Object.class) {

		@Override
		public boolean parsable(String string) {
			return true;
		}

		@Override
		public Object parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return string;
		}
	},
	SHORT(Short.class) {

		@Override
		public boolean parsable(String string) {
			try {
				Short.parseShort(string);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		@Override
		public Short parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return Short.parseShort(string);
		}
	},
	STRING(String.class) {

		@Override
		public boolean parsable(String string) {
			return true;
		}

		@Override
		public String parse(String string) {
			if(!parsable(string)) {
				System.err.println("Data not parsable");
				return null;
			}
			return string;
		}
	};

	private Class<?> typeClass;

	XMLDataType(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	/**
	 * @return the typeClass
	 */
	public Class<?> getTypeClass() {
		return typeClass;
	}

}
