package bindecdiagram;

public class BinDecDiagram {
	
	private Node root;
	private int numOfVariables;
	private int numOfNodes;
	
	public BinDecDiagram() {
		this.root = null;
		this.numOfVariables = 0;
		this.numOfNodes = 0;
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
}
