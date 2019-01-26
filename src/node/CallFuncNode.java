package node;

import function.Function;
import newlang4.*;

import java.util.*;

public class CallFuncNode extends Node {

    /*
    <call_func>	::=
	    <NAME> <LP> <expr_list> <RP>

    <call_sub> ::=
	    <NAME> <expr_list>
     */

    static final Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.NAME
    ));

    private Node args;
    private Node returnVal;
    private String funcName;

    private CallFuncNode(Environment env) {
        super(env, NodeType.FUNCTION_CALL);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception {
        if (FIRST.contains(type)) return new CallFuncNode(env);
        else return null;
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        funcName = la.get().getValue().getSValue();
        LexicalType secondType = la.peekUnit().getType();

        // ver "func LP <expr_list> RP"
        if (secondType == LexicalType.LP) {
            la.get(); // execute "("
            LexicalUnit thirdUnit = la.peekUnit();

            args = ExprListNode.getHandler(thirdUnit.getType(), env);
            parseCheck(args, "syntax error : invalid argument");

            // check last type
            nextCheck(LexicalType.RP, "syntax error : not closed with RP");

            la.get(); // execute ")"
        }
        // ver "func <expr_list>"
        else {
            args = ExprListNode.getHandler(secondType, env);
            parseCheck(args, "syntax error : invalid argument");
        }
        return true;
    }

    public String toString() {
        return funcName + "[" + args + "]";
    }

    public Value getValue() throws Exception{
        if(env.isFuncExist(funcName)){
            Function func = env.getFunction(funcName);
            func.invoke((ExprListNode)args);
        }else{
            throw new Exception("undefined function : " + funcName);
        }
        return null;
    }
}
