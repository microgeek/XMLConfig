package me.microgeek.xmlconfig.config;

public interface DataParser {

	public boolean parsable(String string);

	public Object parse(String string);

}
