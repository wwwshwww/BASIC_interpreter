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

    private ExprListNode(Environment env) {
        super(env, NodeType.EXPR_LIST);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception {
        if (isMatch(type)) return new ExprListNode(env);
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
        while (la.expect(LexicalType.COMMA, 1)) {
            la.get(); // execute ","
            node = ExprNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(node, "syntax error : wrong definition of argument");
            exprList.add(node);
        }

        return true;
    }

    public int getExprCount() {
        return exprList.size();
    }

    public List<Node> getExprList(){
        return exprList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        exprList.stream().forEachOrdered(n -> sb.append(n + ", "));
        return new String(sb);
    }

    public Value getValue() throws Exception {
        exprList.stream().forEach(n -> {
            try {
                n.getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

}
