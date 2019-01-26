package node;

import newlang4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoopBlockNode extends Node {

    /*
    | <WHILE> <cond> <NL> <stmt_list> <WEND> <NL>
	| <DO> <WHILE> <cond> <NL> <stmt_list> <LOOP> <NL>
	| <DO> <UNTIL> <cond> <NL> <stmt_list> <LOOP> <NL>
	| <DO> <NL> <stmt_list> <LOOP> <WHILE> <cond> <NL>
	| <DO> <NL> <stmt_list> <LOOP> <UNTIL> <cond> <NL>
     */

    public static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.WHILE,
            LexicalType.DO
    ));


    boolean condRequired; // true: while, false: until
    boolean isDo; // true: while **, false: do **
    boolean isDoLoop;
    Node cond;
    Node stmtList;

    private LoopBlockNode(Environment env) {
        super(env, NodeType.LOOP_BLOCK);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new LoopBlockNode(env);
        else return null;
    }

    public void parsePrefix() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        // execute "WHILE" or "DO"

        condRequired = la.get().getType() == LexicalType.WHILE;

        cond = CondNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(cond, "invalid cond");

        nextCheck(LexicalType.NL, "without NL after cond");
        la.get(); // execute "NL"
    }

    public void parseContent() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        LexicalType endLoopToken;

        stmtList = StmtListNode.getHandler(la.peekUnit().getType(), env);
        parseCheck(stmtList, "syntax error in LOOP");

        if (isDo) endLoopToken = LexicalType.LOOP;
        else endLoopToken = LexicalType.WEND;

        nextCheck(endLoopToken, "without LOOP in block");
        la.get(); // execute "LOOP" or "WEND"

        if (!isDoLoop) {
            nextCheck(LexicalType.NL, "without NL after LOOP");
            la.get(); // execute "NL"
        }
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();

        // case <WHILE> <cond> <NL> <stmt_list> <WEND> <NL>
        if (la.expect(LexicalType.WHILE, 1)) {
            isDo = false;
            isDoLoop = false;
            parsePrefix();
            parseContent();
        }
        // case <DO> ~
        else if (la.expect(LexicalType.DO, 1)) {
            isDo = true;
            la.get(); // execute "DO"

            if (la.expect(LexicalType.NL, 1)) {
                isDoLoop = true;
                la.get(); // execute "NL"
                parseContent();
                parsePrefix();
            } else if (la.expect(LexicalType.WHILE, 1) || la.expect(LexicalType.UNTIL, 1)) {
                isDoLoop = false;
                parsePrefix();
                parseContent();
            } else {
                throw new Exception("syntax error after DO");
            }
        }
        return true;
    }

    public String toString() {
        String pref, con, content;
        String result;
        con = cond.toString();
        content = "[" + stmtList + "]";

        if (condRequired) {
            pref = "WHILE_LOOP";
        } else {
            pref = "UNTIL_LOOP";
        }
        if (isDoLoop) {
            result = pref + "[" + con + content + "]";
        } else {
            result = pref + "[" + content + con + "]";
        }
        return result;
    }

    public Value getValue() throws Exception {
        boolean judge;

        if (condRequired) judge = true;
        else judge = false;

        if (isDoLoop) stmtList.getValue();

        while (cond.getValue().getBValue() == judge) {
            stmtList.getValue();
        }
        return null;
    }

}
