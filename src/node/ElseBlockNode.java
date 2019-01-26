package node;

import newlang4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ElseBlockNode extends Node {

    public static final Set<LexicalType> FIRST = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.ELSEIF,
            LexicalType.ELSE
    ));

    boolean isElseIf = false;
    boolean isElse = false;

    Node cond;
    Node stmt;
    Node childElse;

    private ElseBlockNode(Environment env) {
        super(env, NodeType.ELSE_BLOCK);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new ElseBlockNode(env);
        else return null;
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        LexicalType type = la.get().getType(); // execute "ELSE" or "ELSEIF"

        // <ELSE> <NL> <stmt_list>
        if (type == LexicalType.ELSE) {
            isElse = true;
            nextCheck(LexicalType.NL, "without NL after ELSE");
            la.get(); // execute "NL"

            stmt = StmtListNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(stmt, "syntax error in ELSE block");
        }
        // <ELSEIF> <cond> <THEN> <NL> <stmt_list>
        else if (type == LexicalType.ELSEIF) {
            isElseIf = true;
            cond = CondNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(cond, "invalid cond");

            nextCheck(LexicalType.THEN, "without THEN in ELSEIF");
            la.get(); // execute "THEN"

            nextCheck(LexicalType.NL, "without NL after THEN");
            la.get(); // execute "NL"

            stmt = StmtListNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(stmt, "syntax error in ELSEIF");

            LexicalType next = la.peekUnit().getType();
            if (ElseBlockNode.isMatch(next)) {
                childElse = ElseBlockNode.getHandler(next, env);
                parseCheck(childElse, "syntax error in block");
            }
        }
        return true;
    }

    public String toString() {
        String result = "";
        if (isElse) {
            result = "ELSE[" + stmt + "]";
        } else if (isElseIf) {
            result = "ELSEIF[" + cond + "[" + stmt + "]]";
            if (childElse != null) {
                result += childElse;
            }
        }
        return result;
    }

    public Value getValue() throws Exception{
        if(isElse) {
            stmt.getValue();
        }else if(isElseIf){
            if(cond.getValue().getBValue()){
                stmt.getValue();
            }else{
                childElse.getValue();
            }
        }
        return null;
    }

}