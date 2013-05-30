package me.microgeek.xmlconfig.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.microgeek.xmlconfig.config.datatypes.CuboidDatatype;
import me.microgeek.xmlconfig.config.datatypes.InventoryDatatype;
import me.microgeek.xmlconfig.config.datatypes.ItemstackDatatype;
import me.microgeek.xmlconfig.config.datatypes.LocationDatatype;
import me.microgeek.xmlconfig.cuboid.Cuboid;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLConfig {

    private Document document;
    private DocumentBuilder documentBuilder;
    private DocumentBuilderFactory documentBuilderFactory;
    private File path;
    private String name;
    private JavaPlugin plugin; 
    private boolean save;
    private Map<String, XMLValue> values = new HashMap<String, XMLValue>();

    /**
     * Setting values does not work, the structure of {@link XMLValue} may need
     * to be changed! Keep in mind, this code is a bit old, and I may optimize
     * it. This was more of a proof of concept
     */
    public XMLConfig(File path, JavaPlugin plugin) {
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(path);
            document.getDocumentElement().normalize();
            loadConfig();
            this.plugin = plugin;
            this.path = path;
            name = getString("name");
            save = getBoolean("save");
        }catch(Exception e) {
            plugin.getLogger().severe("Error creating XMLConfig \"" + path + "\": " + e.getLocalizedMessage());
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
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(path);
            transformer.transform(source, result);
        }catch(Exception e) {
            this.plugin.getLogger().severe("Error saving XMLConfig \"" + path + "\": " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        Element element = document.createElement("data");
        element.setAttribute("value", value.toString());
        // values.put(path, new XMLValue(path, null, this));
    }

    public void addElement(String path, String key, String value) {

    }

    public Object get(String path) {
        XMLValue value = values.get(getAbsolutePath(path));
        if(value == null) {
            return null;
        }
        return value.getValue();
    }

    public Boolean getBoolean(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (Boolean) XMLDataType.BOOLEAN.parse(value.toString());
    }

    public Double getDouble(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (Double) XMLDataType.DOUBLE.parse(value.toString());
    }

    public Float getFloat(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (Float) XMLDataType.FLOAT.parse(value.toString());
    }

    public Short getShort(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (Short) XMLDataType.SHORT.parse(value.toString());
    }

    public Integer getInt(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (Integer) XMLDataType.INTEGER.parse(value.toString());
    }

    public Long getLong(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (Long) XMLDataType.LONG.parse(value.toString());
    }

    public String getString(String path) {
        Object value = get(path);
        if(value == null) return null;
        return (String) XMLDataType.STRING.parse(value.toString());
    }

    public List<String> getStringList(String path) {
        if(path == null) return null;
        return Arrays.asList(getValues(path, false).toArray(new String[0]));
    }

    public Location getLocation(String path) {
        return (Location) new LocationDatatype().parsed(path, this);
    }

    public Cuboid getCuboid(String path) {
        return (Cuboid) new CuboidDatatype().parsed(path, this);
    }

    public ItemStack getItemStack(String path) {
        return (ItemStack) new ItemstackDatatype().parsed(path, this);
    }

    public ItemStack[] getInventory(String path) {
        return (ItemStack[]) new InventoryDatatype().parsed(path, this);
    }

    public Map<String, Object> getKeysAndValues(String path, boolean deep) {
        path = getAbsolutePath(path);
        Map<String, Object> map = new HashMap<String, Object>();
        Node parent = getNode(path);
        if(doesNodeHaveChildren(parent)) {
            for(Node n : getChildNodes(parent, deep)) {
                if(n == parent || (doesNodeHaveChildren(n) && deep)) continue;
                String childPath = getNodePath(n);
                String relativeChildPath = getYongestChild(childPath);
                map.put(relativeChildPath, get(childPath));
            }
        }
        map.remove(path);
        return map;
    }

    public Set<String> getKeys(String path, boolean deep) {
        return getKeysAndValues(path, deep).keySet();
    }

    public Collection<Object> getValues(String path, boolean deep) {
        return getKeysAndValues(path, deep).values();
    }

    public String addPath(String path, String subpath) {
        return path.concat("." + subpath);
    }

    public String getYongestChild(String path) {
        if(path.contains(".")) {
            String[] split = path.split("\\.");
            return split[split.length - 1];
        }
        return path;
    }

    public String getRelativePath(String path) {
        if(path.startsWith("#document.config")) {
            String temp = path.substring("#document.config".length());
            if(temp.startsWith(".")) temp = temp.substring(1);
            return temp;
        }
        return path;
    }

    public String getAbsolutePath(String path) {
        if(!path.startsWith("#document.config")) {
            return path.equals("") ? "#document.config" : addPath("#document.config", path);
        }
        return path;
    }

    public boolean doesNodeHaveChildren(Node node) {
        if(node == null || node.getChildNodes() == null) return false;
        return node.getChildNodes().getLength() > 0;
    }

    public List<Node> getAllNodes() {
        List<Node> nodes = new ArrayList<Node>();
        for(Node n : nodeListToList(document.getChildNodes())) {
            nodes.addAll(getChildNodes(n, true));
        }
        return nodes;
    }

    public List<Node> getChildNodes(Node node, boolean deep) {
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(node);
        for(Node n : nodeListToList(node.getChildNodes())) {
            if(deep) {
                nodes.addAll(getChildNodes(n, deep));
            }else {
                nodes.add(n);
            }
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
        String tmp = getNodeAttribute(node, "key");
        if(tmp == null) {
            return node.getNodeName();
        }
        return tmp;
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
     * @return the path
     */
    public File getFile() {
        return path;
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
     * @param path the path to set
     */
    public void setFile(File file) {
        this.path = file;
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
