package bindecdiagram;

public class Node {
	
	private String value;
	private int level;
	
	private Node[] parents;
	private Node lowChild;
	private Node highChild;
	
	public Node(Node parent, String value, int level) {
		this.parents = new Node[1];
		this.value = value;
		this.level = level;
		this.parents[0] = parent;
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
	
	public Node[] getParents() {
		return this.parents;
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
	
	public Node deleteParent(Node parent) {
		for(int i = 0; i < this.parents.length; i++) {
			if(this.parents[i] != null && this.parents[i].equals(parent)) this.parents[i] = null;
		}
		return null;
	}
	
	public void setParents(Node[] gParents, Node parent) {
		Node[] newParents = new Node[this.parents.length + gParents.length];
		int pos = 0;
		
		for(int i = 0; i < this.parents.length; i++) {
			newParents[pos++] = this.parents[i];
		}
		for(int i = 0; i < gParents.length; i++) {
			newParents[pos++] = gParents[i];
			
			if(parent != null && gParents[i] != null) {
				if(gParents[i].getLow().equals(parent)) gParents[i].setLow(this);
				else gParents[i].setHigh(this);
			}
		}
		this.parents = newParents;
	}
}
