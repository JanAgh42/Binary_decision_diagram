import java.util.Scanner;

import bindecdiagram.BinDecDiagram;
import bindecdiagram.DiagramCreator;

public class Main {
	
	static Scanner s = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Boolean expression in DNF form (example: Ab+BC, where A,B,C=normal, b=invert): ");
		String expression = s.nextLine();
		
		System.out.println("Order of variables (example: BCA): ");
		String order = s.nextLine();
		//System.out.println(order.length());
		
		DiagramCreator creator = new DiagramCreator(expression, order);
		
		BinDecDiagram diagram = creator.BDD_create();
		
		System.out.println("Input (example: 1010): ");
		
		String input = s.nextLine();
		
		String result = creator.BDD_use(diagram, input);
		System.out.println(result);
	}
}
