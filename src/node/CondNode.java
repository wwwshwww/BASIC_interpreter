package node;

import newlang4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CondNode extends Node {

    public static final Set<LexicalType> FIRST = new HashSet<LexicalType>() {{
        addAll(ExprNode.FIRST);
    }};

    public static final Set<LexicalType> COND = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.EQ,
            LexicalType.GT,
            LexicalType.LT,
            LexicalType.GE,
            LexicalType.LE,
            LexicalType.NE
    ));

    Node left, right;
    LexicalUnit condUnit;

    private CondNode(Environment env) {
        super(env, NodeType.COND);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if(isMatch(type))return new CondNode(env);
        else return null;
    }

    public boolean parse() throws Exception{
        LexicalAnalyzerImpl la = env.getInput();

        left = ExprNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(left, "invalid expression on left");

        condUnit = la.get();
        if(!COND.contains(condUnit.getType())){
            throw new Exception("not found conditional expression");
        }

        right = ExprNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(right, "invalid expression on left");

        return true;
    }

}
