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

    private Node childList, expr;

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

        expr = ExprNode.getHandler(firstType, env);
        parseCheck(expr, "invalid argument");

        // check comma
        if(la.expect(LexicalType.COMMA, 1)) {
            la.get();
            LexicalUnit following = la.peekUnit();
            childList = ExprListNode.getHandler(following.getType(), env);
            parseCheck(childList, "syntax error : wrong definition of argument");

        }

        return true;
    }

}
