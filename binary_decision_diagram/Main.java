import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import bindecdiagram.BinDecDiagram;
import bindecdiagram.DiagramCreator;
import bindecdiagram.ExpSimplifier;

public class Main {
	
	static Scanner s = null;
	static Random r = null;
	
	static String[] expressions = null;
	
	static BinDecDiagram diagram = null;
	static DiagramCreator creator = null;

	public static void main(String[] args) {	
		s = new Scanner(System.in);
		r = new Random();
		int decision = -1;
	
		do {
			System.out.println("--------------------------------------");
			System.out.println("***Make sure to only enter functions in correct form***");
			System.out.println("***Keep variables inside expressions in alphabetical order***");
			System.out.println("***Do not enter expressions with duplicate variables ('AaB+bCC')***");
			System.out.println("1 - Manual testing");
			System.out.println("2 - Manual testing - evaluate all cases");
			System.out.println("3 - Random testing");
			System.out.println("4 - Automatic testing - 100 iterations");
			System.out.println("5 - Performance testing");
			System.out.println("0 - Exit");
			System.out.print("Your choice: ");
			
			try {
				decision = s.nextInt();
				
				switch(decision) {
					case 1: manual(false, false); break;
					case 2: manual(true, false); break;
					case 3: manual(true, true); break;
					case 4: automatic(); break;
					case 5: performance(); break;				
				}
			} catch(InputMismatchException ime) {
				s.nextLine();
			}
		}while(decision != 0);
		s.close();
	}
	
	static void manual(boolean eval, boolean random) {
		String expression = "", order = "";
		int varAm = 0;
		
		if(!random) {
			s.nextLine();
			System.out.println("Boolean expression in DNF form (invert = lowercase): ");
			expression = s.nextLine();
			
			System.out.println("Order of variables (example: BCA): ");
			order = s.nextLine();
		}
		else {
			System.out.print("Amount of different variables in expression: ");
			varAm = s.nextInt();
			
			order = generateExpressions(1, varAm);
			expression = expressions[0];
		}
		
		System.out.println("******************************");
		creator = new DiagramCreator();
		diagram = creator.BDD_create(expression, order);
		
		if(diagram == null) {
			System.out.println("Invalid order of variables!"); return;
		}
		diagram.traverseDiagram();
		System.out.println("******************************");
		
		System.out.println("Before reduction: " + (int) diagram.getMaxNodeCount() + " nodes");
		System.out.println("After reduction: " + (int) diagram.getRealNodeCount() + " nodes");
		System.out.println("Reduction rate: " + (100 - (diagram.getRealNodeCount() / diagram.getMaxNodeCount()) * 100) + "%");
		System.out.println("******************************");
		
		if(eval) automaticEvaluation(true);
		else manualEvaluation();
	}
	
	static void automaticEvaluation(boolean write) {
		if(write) System.out.println(diagram.getVarTypes() + "  BDD_use   Eval");
		
		int length = diagram.getVarTypes().length(), counter = 0;
		
		for(int i = 0; i < Math.pow(2, length); i++) {
			String binary = String.format("%" + length + "s", Integer.toBinaryString(i)).replaceAll(" ", "0");
			
			String BDDres = creator.BDD_use(diagram, binary);
			String EvalRes = evaluateExpression(diagram.getOrig(), diagram.getVarTypes(), binary);
			if(BDDres.equals(EvalRes)) counter++;
			
			if(write) System.out.println(binary + ": " + BDDres + " ------- " + EvalRes);
		}
		System.out.println("******************************");
		if(!write) System.out.println(diagram.getOrig());
		System.out.println(counter + "/" + (int) Math.pow(2, length) + " results are correct.");
	}
	
	static void manualEvaluation() {
		System.out.println("Input vector (example: 1010): ");
		String input = s.nextLine();
		
		System.out.println("BDD_use result: " + creator.BDD_use(diagram, input));
		System.out.println("Evaluation result: " + evaluateExpression(diagram.getOrig(), diagram.getVarTypes(), input));
	}
	
	static void automatic() {
		System.out.print("Amount of different variables in each expression: ");
		int varAm = s.nextInt();
		String order = "";
		
		for(int i = 0; i < 100; i++) {
			order = generateExpressions(1, varAm);
			
			if(!expressions[0].isBlank()) {
				creator = new DiagramCreator();
				diagram = creator.BDD_create(expressions[0], order);
				automaticEvaluation(false);
			}
			
		}
	}
	
	static void performance() {
		System.out.print("Amount of expressions to be generated: ");
		int expAm = s.nextInt();
		
		System.out.print("Amount of different variables in each expression: ");
		int varAm = s.nextInt();
		
		String order = generateExpressions(expAm, varAm);
		double totalNodes = 0, reducedNodes = 0;
	
		long begin = System.nanoTime();
		for(int x = 0; x < expAm; x++) {
			if(!expressions[x].isBlank()) {
				diagram = new DiagramCreator().BDD_create(expressions[x], order);
				totalNodes += diagram.getMaxNodeCount();
				reducedNodes += diagram.getRealNodeCount();
			}
		}
		long end = System.nanoTime();
		System.out.println("******************************");
		System.out.println("Execution time: " + (end - begin) / 1000000 + "ms");
		System.out.println("******************************");
		System.out.println("Total number of nodes combined: " + (int) totalNodes);
		System.out.println("Number of nodes combined after reduction: " + (int) reducedNodes);
		System.out.println("Reduction rate: " + (100 - (reducedNodes / totalNodes) * 100) + "%");
	}
	
	static String evaluateExpression(String expression, String order, String input) {
		String[] letters = order.split("");
		String[] numbers = input.split("");
		String[] pieces = expression.split("");
		
		if(letters.length != numbers.length || !ExpSimplifier.instance().validateInp(input)) return "-1";
		
		for(int i = 0; i < letters.length; i++) {
			
			String lowercase = letters[i].toLowerCase();
			
			for(int j = 0; j < pieces.length; j++) {
				if(pieces[j].equals(letters[i])) pieces[j] = numbers[i];
				else if(pieces[j].equals(lowercase)) pieces[j] = numbers[i].equals("1") ? "0" : "1";
			}
		}
		
		for(int j = 0; j < pieces.length; j++) {
			if(!pieces[j].equals("0") && !pieces[j].equals("1") && !pieces[j].equals("+")) return null;
		}
		expression = String.join("", pieces);
		pieces = expression.contains("+") ? expression.split("\\+") : new String[] {expression};
		
		for(int i = 0; i < pieces.length; i++) {
			if(!pieces[i].contains("0")) return "1";
		}
		return "0";
	}
	
	static String generateExpressions(int amount, int numOfVars) {
		expressions = new String[amount];
		String order = "";
		
		for(int i = 0; i < amount; i++) {
			int parts = r.nextInt(20) + 1;
			String[] exp = new String[parts];
			String nExp = "";
			Arrays.fill(exp, "");
			
			for(int j = 65; j < 65 + numOfVars; j++) {
				for(int k = 0; k < exp.length; k++) {
					boolean invert = r.nextBoolean();
					boolean hasLetter = r.nextBoolean();
					
					if(hasLetter) exp[k] += (invert ? (char)(j + 32) : (char) j);
				}
				order += i > 0 ? "" : (char) j;
			}
			for(int j = 0; j < parts; j++) {
				if(exp[j].isBlank()) continue;
				
				nExp += exp[j] + (j + 1 < parts && j + 1 != parts ? "+" : "");
			}
			if(!nExp.isBlank() && nExp.charAt(nExp.length() - 1) == '+') nExp = nExp.substring(0, nExp.length() - 1);
			expressions[i] = nExp;
		}
		return order;
	}
}
