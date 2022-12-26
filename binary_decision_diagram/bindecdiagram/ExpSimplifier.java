package bindecdiagram;

public class ExpSimplifier {
	
	private static ExpSimplifier instance;
	
	private ExpSimplifier() {}

	public static ExpSimplifier instance() {
		if(instance == null) instance = new ExpSimplifier();
		return instance;
	}
	
	public String newExp(String expression, String upper, boolean flag) {
		String lower = upper.toLowerCase();
		
		if(expression.equals("1") || expression.equals("0")) return expression;
		String[] parts = expression.contains("+") ? expression.split("\\+") : new String[] {expression};
		String[] hash = new String[parts.length * 2];
		
		for(String part: parts) {
			if((flag && part.equals(lower)) || (!flag && part.equals(upper))) return "1";
		}
		if((flag && expression.equals(lower)) || (!flag && expression.equals(upper))) return "1";
		if((!flag && expression.equals(lower)) || (flag && expression.equals(upper))) return "0";
		int temp = 0, count = 0;
	 	
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].isBlank()) continue;
			if(parts[i].contains(flag ? upper : lower)) {
				if(parts[i].contains(lower) && parts[i].contains(upper)) {
					if(++count == parts.length) return "0";
				}
				parts[i] = "";
				temp++;
			}
			else if(parts[i].contains(flag ? lower : upper)) {
				parts[i] = this.removeLetter(parts[i], flag ? lower : upper);
				if(parts[i].isBlank()) temp++;
			}
		}
		temp = parts.length - temp - 1;
		
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].isBlank()) continue;
			
			int pos = HashCreator.instance().hash(parts[i], hash.length);
			while(hash[pos] != null) {
				if(hash[pos].equals(parts[i])) break;
				if(++pos >= hash.length) pos = 0;
			}
			if(hash[pos] != null) {
				parts[i] = ""; temp--;
			}
			else hash[pos] = parts[i];
		}
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
	
	public boolean validateInp(String input) {
		String[] nums = input.split("");
		
		for(String num: nums) {
			if(!num.equals("0") && !num.equals("1")) return false;
		}
		return true;
	}
	
	public boolean validateVarTypes(String expression, String varTypes) {
		
		String[] exp = expression.toUpperCase().split("");
		
		for(int i = 0; i < varTypes.length(); i++) {
			String letter = Character.toString(varTypes.charAt(i));
			
			for(int j = 0; j < exp.length; j++) {
				if(exp[j].equals(letter) || exp[j].equals("+")) exp[j] = "0"; 
			}
		}
		for(int j = 0; j < exp.length; j++) {
			if(!exp[j].equals("0")) return false; 
		}
		return true;
	}
	
	private String removeLetter(String exp, String letter) {
		exp = exp.replace(letter, "");
		return exp.contains(letter) ? this.removeLetter(exp, letter) : exp;
	}
}
