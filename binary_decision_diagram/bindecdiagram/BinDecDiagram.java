package bindecdiagram;

public class BinDecDiagram {
	
	private Node root;
	private String originalExpression;
	private String varTypes;
	private double realNumOfNodes;
	private double maxNumOfNodes;
	
	public BinDecDiagram(String varTypes, String original) {
		this.originalExpression = original;
		this.root = null;
		this.realNumOfNodes = 1;
		this.maxNumOfNodes = 0;
		this.varTypes = varTypes;
		
		this.calculateNodes(this.varTypes.length());
	}
	
	private void calculateNodes(int max) {
		for(int i = 0; i < max; i++) {
			this.maxNumOfNodes += Math.pow(2, i);
		}
	}
	
	public void countNode() {
		this.realNumOfNodes += 2;
	}
	
	public void subtractNode() {
		this.realNumOfNodes -= 1;
	}
	
	public double getRealNodeCount() {
		return this.realNumOfNodes;
	}
	
	public double getMaxNodeCount() {
		return this.maxNumOfNodes;
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public String getVarTypes() {
		return this.varTypes;
	}
	
	public String getOrig() {
		return this.originalExpression;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public void traverseDiagram() {
		this.traverse(this.root, this.root.getLevel());
	}
	
	private void traverse(Node node, int level) {
		if(node != null) {	
			this.traverse(node.getHigh(), (node.getHigh() == null ? this.varTypes.length() : node.getHigh().getLevel()));
			for(int i = 0; i < level; i++) {
				System.out.print("      ");
			}
			System.out.println((level == 0 ? "" : this.varTypes.charAt(level - 1) + ": ") +  "(" + node.getValue() + ")" + " -- " + node + "\n");
			this.traverse(node.getLow(), (node.getLow() == null ? this.varTypes.length() : node.getLow().getLevel()));
		}
	}
}
