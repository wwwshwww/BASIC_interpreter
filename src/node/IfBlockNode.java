package node;

import newlang4.Environment;
import newlang4.LexicalAnalyzerImpl;
import newlang4.LexicalType;
import newlang4.NodeType;

import java.util.*;

public class IfBlockNode extends Node {

    public static final Set<LexicalType> FIRST = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.IF
    ));

    private Node cond;
    private Node stmt;
    private Node elseStmt;
    private Node childElse;

    private IfBlockNode(Environment env) {
        super(env, NodeType.IF_BLOCK);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new IfBlockNode(env);
        else return null;
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();

        la.get(); // execute "IF"

        cond = CondNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(cond, "invalid cond");

        nextCheck(LexicalType.THEN, "without THEN in IF");
        la.get(); // execute "THEN"

        // case <if_prefix> <NL> <stmt_list> <else_block> <ENDIF> <NL>
        if (la.expect(LexicalType.NL, 1)) {
            nextCheck(LexicalType.NL, "without NL after THEN");
            la.get(); // execute "NL"

            stmt = StmtListNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(stmt, "error in IF");

            if (!la.expect(LexicalType.ENDIF, 1)) {
                childElse = ElseBlockNode.getHandler(la.peekUnit().getType(), env);
                parseCheck(childElse, "syntax error");
            }

            nextCheck(LexicalType.ENDIF, "without ENDIF in IF");
            la.get(); // execute "ENDIF"

            nextCheck(LexicalType.NL, "without NL after ENDIF");
            la.get(); // execute "NL"
        }
        // case <if_prefix> <stmt> <NL>
        //	    <if_prefix> <stmt> <ELSE> <stmt> <NL>
        else {
            stmt = StmtNode.getHandler(la.peekUnit().getType(), env);
            parseCheck(stmt, "error in IF");

            if (la.expect(LexicalType.NL, 1)) {
                la.get(); // execute "NL"
            } else {
                nextCheck(LexicalType.ELSE, "syntax error");
                la.get(); // execute "ELSE"
                elseStmt = StmtNode.getHandler(la.peekUnit().getType(), env);
                parseCheck(elseStmt, "error in ELSE");

                nextCheck(LexicalType.NL, "without NL after ELSE case");
                la.get(); // execute "NL"
            }
        }

        return true;

    }

    public String toString() {
        String result = "IF[" + cond + "[" + stmt + "]]";
        if (elseStmt != null) {
            result += "ELSE" + "[" + elseStmt + "]";
        } else if (childElse != null) {
            result += childElse;
        }

        return result;

    }
}