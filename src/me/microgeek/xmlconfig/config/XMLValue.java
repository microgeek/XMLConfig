package me.microgeek.xmlconfig.config;

import org.w3c.dom.Node;

public class XMLValue {

	private final XMLConfig config;
	private final Node node;
	private String path;

	public XMLValue(String path, Node node, XMLConfig config) {
		this.path = path;
		this.node = node;
		this.config = config;
	}

	public Object getValue() {
		return config.getNodeValue(node);
	}

	/**
	 * @return the config
	 */
	public XMLConfig getConfig() {
		return config;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
