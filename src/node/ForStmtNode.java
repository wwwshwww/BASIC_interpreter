package node;

import newlang4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ForStmtNode extends Node {

    // <FOR> <subst> <TO> <INTVAL> <NL> <stmt_list> <NEXT> <NAME>

    Node controlVar;
    Node toVal;
    Node stmtList;
    int startVal, endVal;

    public static Set<LexicalType> FIRST = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.FOR
    ));

    private ForStmtNode(Environment env) {
        super(env, NodeType.FOR_STMT);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (ForStmtNode.isMatch(type)) return new ForStmtNode(env);
        else return null;
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();

        la.get(); // execute "FOR"

        controlVar = SubstNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(controlVar, "invalid expression in FOR declaration");

        nextCheck(LexicalType.TO, "without TO in FOR declaration");
        la.get(); // execute "TO"

        toVal = ExprNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(toVal, "invalid expression in FOR declaration");

        la.goodByeNL(); // execute any "NL"
        stmtList = StmtListNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(stmtList, "error in FOR");

        nextCheck(LexicalType.NEXT, "without NEXT in FOR");
        la.get(); // execute "NEXT"

        nextCheck(LexicalType.NAME, "without variable after NEXT");

        String nextName = la.get().getValue().getSValue(); // execute NAME
        String contrlName = ((SubstNode) controlVar).getVarName();

        if (!nextName.equals(contrlName)) {
            throw new Exception("vary variable in NEXT : " + la.getLine());
        }

        return true;
    }

    public String toString() {
        return "FOR[[" + controlVar + "->" + toVal + "][" + stmtList + "]]";
    }

    public Value getValue() throws Exception {
        controlVar.getValue();

        return null;
    }

}
