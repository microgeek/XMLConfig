package me.microgeek.xmlconfig.config.datatypes;

import me.microgeek.xmlconfig.config.XMLConfig;


public interface Datatype {

    public Object parsed(String path, XMLConfig config);
    
}
