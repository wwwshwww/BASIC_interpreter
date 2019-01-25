package node;

import newlang4.*;

import java.util.*;

public class ExprListNode extends Node {

    /*
    <expr_list>  ::=
	    <expr>
	    | <expr_list> <COMMA> <expr>
     */

    static final Set<LexicalType> FIRST = new HashSet<LexicalType>() {{
        addAll(ExprNode.FIRST);
    }};

    private List<Node> exprList = new ArrayList<>();

    private ExprListNode(Environment env){
        super(env, NodeType.EXPR_LIST);
    }

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception {
        if(isMatch(type)) return new ExprListNode(env);
        else return null;
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        LexicalType firstType = la.peekUnit().getType();
        Node node;

        node = ExprNode.getHandler(firstType, env);
        parseCheck(node, "invalid argument");
        exprList.add(node);

        // check comma
        while(la.expect(LexicalType.COMMA, 1)) {
            la.get(); // execute ","
            node = ExprListNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(node, "syntax error : wrong definition of argument");
            exprList.add(node);
        }

        return true;
    }

    public String toString(){
        String result = "";
        for (Node node : exprList) {
            result += node + ", ";
        }
        return result;
    }

}
