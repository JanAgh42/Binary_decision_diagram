package bindecdiagram;

public class Node {
	
	private String value;
	private int level;
	
	private Node parent;
	private Node lowChild;
	private Node highChild;
	
	public Node(Node parent, String value, int level) {
		this.parent = parent;
		this.value = value;
		this.level = level;
	}
	
	public Node getLow() {
		return this.lowChild;
	}
	
	public Node getHigh() {
		return this.highChild;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public int getLevel() {
		return this.level;
	}

	public void setLow(Node lowChild) {
		this.lowChild = lowChild;
	}
	
	public void setHigh(Node highChild) {
		this.highChild = highChild;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
}
