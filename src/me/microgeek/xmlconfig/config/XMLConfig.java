package me.microgeek.xmlconfig.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLConfig {

	private Document document;
	private DocumentBuilder documentBuilder;
	private DocumentBuilderFactory documentBuilderFactory;
	private File file;
	private String name;
	private JavaPlugin plugin;
	private boolean save;
	private Map<String, XMLValue> values = new HashMap<String, XMLValue>();

	/**
	 * Setting values does not work, the structure of {@link XMLValue} may need to be changed!
	 * Keep in mind, this code is a bit old, and I may optimize it. This was more of a proof of concept
	 */
	public XMLConfig(String name, boolean save, JavaPlugin plugin) {
		try {
			this.name = name;
			this.save = save;
			this.plugin = plugin;
			file = new File(this.plugin.getDataFolder(), name + ".xml");
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(file);
			document.getDocumentElement().normalize();
			loadConfig();
		}catch(Exception e) {
			this.plugin.getLogger().severe("Error creating XMLConfig \"" + this.name + "\": " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void loadConfig() {
		for(Node n : getAllNodes()) {
			String path = getNodePath(n);
			values.put(path, new XMLValue(path, n, this));
		}
		XMLWrapper.XML_CONFIGS.put(name, this);
	}
	
	public void saveConfig() {

	}
	
	public void set(String path, Object value) {
		Element element = document.createElement("data");
		element.setAttribute("value", value.toString());
		//values.put(path, new XMLValue(path, null, this));
	}

	public Object get(String path) {
		XMLValue value = values.get(path);
		if(value == null) {
			return null;
		}
		return value.getValue();
	}

	public boolean getBoolean(String path) {
		return (Boolean) XMLDataType.BOOLEAN.parse(get(path).toString());
	}

	public double getDouble(String path) {
		return (Double) XMLDataType.DOUBLE.parse(get(path).toString());
	}

	public float getFloat(String path) {
		return (Float) XMLDataType.FLOAT.parse(get(path).toString());
	}

	public int getInt(String path) {
		return (Integer) XMLDataType.INTEGER.parse(get(path).toString());
	}

	public String getString(String path) {
		return (String) XMLDataType.STRING.parse(getNodeValue(getNode(path)));
	}

	/**
	 * Was more of a test, needs to moved over to XMLDataType.LOCATION, read comment in {@link XMLDataType}
	 */
	public Location getLocation(String path) {
		World world = plugin.getServer().getWorld(getString(addPath(path, "world")));
		double x = getDouble(addPath(path, "x"));
		double y = getDouble(addPath(path, "y"));
		double z = getDouble(addPath(path, "z"));
		float yaw = getFloat(addPath(path, "yaw"));
		float pitch = getFloat(addPath(path, "pitch"));
		return new Location(world, x, y, z, yaw, pitch);
	}

	public String addPath(String path, String subpath) {
		return path.concat("." + subpath);
	}

	public boolean doesNodeHaveChildren(Node node) {
		return node.getChildNodes().getLength() > 0;
	}

	public List<Node> getAllNodes() {
		List<Node> nodes = new ArrayList<Node>();
		for(Node n : nodeListToList(document.getChildNodes())) {
			nodes.addAll(getChildNodes(n));
		}
		return nodes;
	}

	public List<Node> getChildNodes(Node node) {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(node);
		for(Node n : nodeListToList(node.getChildNodes())) {
			nodes.addAll(getChildNodes(n));
		}
		return nodes;
	}

	public Node getNode(String path) {
		XMLValue value = values.get(path);
		if(value == null) {
			return null;
		}
		return value.getNode();
	}

	public String getNodeAttribute(Node node, String attribute) {
		if(node == null) {
			return null;
		}
		if(node.getAttributes() == null) {
			return null;
		}
		Node value = node.getAttributes().getNamedItem(attribute);
		if(value == null) {
			return null;
		}
		return value.getTextContent();
	}

	public String getNodeKey(Node node) {
		return getNodeAttribute(node, "key");
	}

	public String getNodePath(Node node) {
		String path = getNodeKey(node);
		Node parent = node.getParentNode();
		while(parent != null && getNodeKey(parent) != null) {
			path = getNodeKey(parent) + "." + path;
			parent = parent.getParentNode();
		}
		return path;
	}

	public String getNodeType(Node node) {
		return getNodeAttribute(node, "type");
	}

	public String getNodeValue(Node node) {
		String attribute = getNodeAttribute(node, "value");
		if(attribute != null) {
			return attribute;
		}
		return node.getTextContent();
	}

	public Node getParentNode(String path) {
		return getNode(path.substring(0, path.lastIndexOf(".")));
	}

	public List<Node> nodeListToList(NodeList nodeList) {
		List<Node> nodes = new ArrayList<Node>();
		for(int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if(n.getNodeType() == Node.TEXT_NODE) {
				continue;
			}
			nodes.add(n);
		}
		return nodes;
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @return the documentBuilder
	 */
	public DocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}

	/**
	 * @return the documentBuilderFactory
	 */
	public DocumentBuilderFactory getDocumentBuilderFactory() {
		return documentBuilderFactory;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}

	/**
	 * @return the values
	 */
	public Map<String, XMLValue> getValues() {
		return values;
	}

	/**
	 * @return the save
	 */
	public boolean isSave() {
		return save;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param plugin the plugin to set
	 */
	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * @param save the save to set
	 */
	public void setSave(boolean save) {
		this.save = save;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Map<String, XMLValue> values) {
		this.values = values;
	}

}
