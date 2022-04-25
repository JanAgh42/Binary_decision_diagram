package bindecdiagram;

import java.util.ArrayList;

public class DiagramCreator {
	
	private BinDecDiagram diagram;
	
	private String expression;
	private String order;
	private String[] varTypes;
	
	private Node[] previous;
	private Node[] current;
		
	public DiagramCreator(String expression, String order) {
		this.expression = expression;
		this.order = order;
		this.varTypes = this.order.split("");
		
		this.previous = new Node[1];
		this.current = new Node[2];
	}

	public BinDecDiagram BDD_create() {
		this.diagram = new BinDecDiagram();
		Node node = new Node(null, this.expression, 0);
		diagram.setRoot(node);
		this.previous[0] = node;
		int num, pos = 1;
		
		while(pos <= this.varTypes.length) {
			num = 0;
			for(int i = 0; i < this.previous.length; i++) {
				if(this.previous[i] == null) continue;
				Node low = this.reduceExp(this.previous[i], this.previous[i].getValue(), pos, true);
				Node high = this.reduceExp(this.previous[i], this.previous[i].getValue(), pos, false);

				this.previous[i].setLow(low);
				this.previous[i].setHigh(high);
				this.current[num++] = low;
				this.current[num++] = high;
			}
			this.handleReduction();
			for(int i = 0; i < this.previous.length; i++) {
				if(this.previous[i] != null) {
					System.out.print(this.previous[i].getValue() + " ");
				}
			}
			System.out.println();
			pos++;
			this.previous = this.current;
			this.current = new Node[this.previous.length * 2];
		}
		for(int i = 0; i < this.previous.length; i++) {
			if(this.previous[i] != null) {
				System.out.print(this.previous[i].getValue() + " ");
			}
		}
		return this.diagram;
	}
	
	private void handleReduction() {
		for(int i = 0; i < this.current.length - 1; i++) {
			if(this.current[i] == null) continue;
			for(int j = i + 1; j < this.current.length; j++) {
				if(this.current[j] == null) continue;
				if(this.current[i].getValue().equals(this.current[j].getValue())) this.deleteNode(i, j);
			}
		}
		
		for(int i = 0; i < this.previous.length; i++) {
			if(this.previous[i] == null) continue;

			Node low = this.previous[i].getLow();
			Node high = this.previous[i].getHigh();
			Node parent = this.previous[i].getParent();
			
			if(low.equals(high)) {
				low.setParent(parent);
				if(parent != null) {
					if(parent.getLow().equals(this.previous[i])) parent.setLow(low);
					else parent.setHigh(low);
				}
				else if(this.previous[i].equals(this.diagram.getRoot())) {
					this.diagram.setRoot(low);
				}
				this.previous[i] = null;
			}
		}
	}
	
	private void deleteNode(int first, int second) {
		Node parent = this.current[second].getParent();
		
		if(parent.getLow().equals(this.current[second])) parent.setLow(this.current[first]);
		else parent.setHigh(this.current[first]);
		this.current[second] = null;
	}
	
	private Node reduceExp(Node parent, String expression, int varPos, boolean flag) {
		if(varPos == this.varTypes.length + 1) {
			return new Node(parent, expression.equals("1") ? "1" : "0", this.varTypes.length);
		}
		String newExp = this.findExpression(expression, varPos - 1, flag);
		
		return new Node(parent, newExp, varPos);
	}
	
	private String findExpression(String expression, int variable, boolean flag) {
		String upper = this.varTypes[variable];
		String lower = upper.toLowerCase();
		
		if(expression.equals("1") || expression.equals("0")) return expression;
		String[] parts = expression.contains("+") ? expression.split("\\+") : new String[] {expression};
		
		for(String part: parts) {
			if((flag && part.equals(lower)) || (!flag && part.equals(upper))) return "1";
		}
		
		if((flag && expression.equals(lower)) || (!flag && expression.equals(upper))) return "1";
		if((!flag && expression.equals(lower)) || (flag && expression.equals(upper))) return "0";
		int temp = 0, count = 0;
	 	
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].contains(flag ? upper : lower)) {
				if(parts[i].contains(lower) && parts[i].contains(upper)) {
					if(++count == parts.length) return "0";
				}
				parts[i] = "";
				temp++;
			}
			else if(parts[i].contains(flag ? lower : upper)) {
				parts[i] = this.removeLetter(parts[i], flag ? lower : upper);
				if(parts[i].equals("")) temp++;
			}
		}
		temp = parts.length - temp - 1;
		lower = expression; 
		expression = "";
		
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].isBlank()) continue;
			else expression += parts[i];
				
			if(temp-- > 0) expression += "+";
		}
		if(expression.isBlank()) {
			if(lower.contains(upper)) expression = flag ? "0" : "1";
			else expression = flag ? "1" : "0";
		}
		return expression;
	}
	
	private String removeLetter(String exp, String letter) {
		exp = exp.replace(letter, "");
		return exp.contains(letter) ? this.removeLetter(exp, letter) : exp;
	}
	
	public String BDD_use(BinDecDiagram diagram, String input) {
		Node current = diagram.getRoot();
		String[] parts = input.split("");
		int pos = current.getLevel();
		
		while(pos < this.varTypes.length) {
			int var = current.getLevel();
			if(pos == var) current = parts[var].equals("0") ? current.getLow() : current.getHigh();
			pos++;
		}
		return current.getValue();
	}
}
