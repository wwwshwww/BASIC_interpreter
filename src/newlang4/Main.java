package newlang4;

import java.io.FileInputStream;

import node.Node;
import node.ProgramNode;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fin = null;
		LexicalAnalyzerImpl lex;
		LexicalUnit first;
		Environment env;
		Node program;

		System.out.println("basic parser");
		fin = new FileInputStream("test1.bas");
		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);
		first = lex.peekUnit();

		program = ProgramNode.getHandler(first.getType(), env);

		if (program != null && program.parse()) {
			System.out.println(program);
			//System.out.println("value = " + program.getValue());
		} else {
			System.out.println("syntax error");
		}
	}

}
