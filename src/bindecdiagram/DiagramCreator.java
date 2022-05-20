package bindecdiagram;

public class DiagramCreator {
	
	private String expression;
	private String order;
	private String[] varTypes;
	
	private Node[] previous;
	private Node[] current;
		
	public DiagramCreator() {	
		this.previous = new Node[1];
		this.current = new Node[2];
	}

	public BinDecDiagram BDD_create(String expression, String order) {
		this.expression = expression;
		this.order = order.toUpperCase();
		this.varTypes = this.order.split("");
		
		if(!ExpSimplifier.instance().validateVarTypes(expression, order) || expression.isBlank()) return null;
		
		BinDecDiagram diagram = new BinDecDiagram(order, expression);
		diagram.setRoot(this.previous[0] = new Node(null, this.expression, 0));
		int num, pos = 0;
		
		while(++pos <= this.varTypes.length) {
			num = 0;
			for(int i = 0; i < this.previous.length; i++) {
				if(this.previous[i] == null) continue;
				
				Node low = this.reduceExp(this.previous[i], this.previous[i].getValue(), pos, true);
				Node high = this.reduceExp(this.previous[i], this.previous[i].getValue(), pos, false);

				this.previous[i].setLow(this.current[num++] = low);
				this.previous[i].setHigh(this.current[num++] = high);
				if(pos != this.varTypes.length) diagram.countNode();
			}
			this.handleReduction(diagram);
			this.previous = this.current;
			this.current = new Node[this.previous.length * 2];
		}
		return diagram;
	}
	
	private void handleReduction(BinDecDiagram diagram) {
		Node[] hash = new Node[this.current.length * 2];
		
		for(int i = 0; i < this.current.length; i++) {
			if(this.current[i] == null) continue;
			int pos = HashCreator.instance().hash(this.current[i].getValue(), hash.length);
			
			while(hash[pos] != null) {
				if(hash[pos].getValue().equals(this.current[i].getValue())) break;
				if(++pos >= hash.length) pos = 0;
			}
			if(hash[pos] != null) {
				this.deleteNode(hash[pos], i);
				if(hash[pos].getLevel() != this.varTypes.length) diagram.subtractNode();
			}
			else hash[pos] = this.current[i];
		}
		
		for(int i = 0; i < this.previous.length; i++) {
			if(this.previous[i] == null) continue;

			Node low = this.previous[i].getLow();
			Node[] parents = this.previous[i].getParents();
			
			if(low.equals(this.previous[i].getHigh())) {
				diagram.subtractNode();
				if (this.previous[i].equals(diagram.getRoot())) diagram.setRoot(low);
				low.setParents(parents, this.previous[i]);
				this.previous[i] = low.deleteParent(this.previous[i]);
			}
		}
	}

	private void deleteNode(Node first, int second) {
		Node[] parents = this.current[second].getParents();
		
		for(int i = 0; i < parents.length; i++) {
			if(parents[i] != null) {
				if(parents[i].getLow().equals(this.current[second])) parents[i].setLow(first);
				else parents[i].setHigh(first);
				first.setParents(parents, null);
			}
		}
		this.current[second] = null;
	}
	
	private Node reduceExp(Node parent, String expression, int varPos, boolean flag) {
		if(varPos == this.varTypes.length + 1) {
			return new Node(parent, expression.equals("1") ? "1" : "0", this.varTypes.length);
		}
		String newExp = ExpSimplifier.instance().newExp(expression, this.varTypes[varPos - 1], flag);
		
		return new Node(parent, newExp, varPos);
	}
	
	public String BDD_use(BinDecDiagram diagram, String input) {
		Node current = diagram.getRoot();
		String[] parts = input.split("");
		int pos = current.getLevel() - 1;
		
		if(parts.length != diagram.getVarTypes().length() || !ExpSimplifier.instance().validateInp(input)) return "-1";
		
		while(++pos < diagram.getVarTypes().length()) {
			int var = current.getLevel();
			if(pos == var) current = parts[var].equals("0") ? current.getLow() : current.getHigh();
		}
		return current.getValue();
	}
}
