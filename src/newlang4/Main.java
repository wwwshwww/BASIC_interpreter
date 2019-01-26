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
        String path = "test1.bas";
        if (args.length > 0) {
            path = args[0];
        }

        System.out.println("basic parser");
        fin = new FileInputStream(path);
        lex = new LexicalAnalyzerImpl(fin);
        env = new Environment(lex);
        first = lex.peekUnit();

        program = ProgramNode.getHandler(first.getType(), env);

        if (program != null && program.parse()) {
            System.out.println(program);
            //System.out.println(program.getValue());
        } else {
            System.out.println("syntax error : 1");
        }
    }

}
