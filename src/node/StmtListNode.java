package node;

import analyzer.Environment;
import analyzer.LexicalType;
import analyzer.NodeType;
import analyzer.Value;

import java.util.*;

public class StmtListNode extends Node {

    List<Node> child = new ArrayList<>();
	
	/*
	<stmt_list> ::=
		<stmt>
		| <stmt_list> <NL> <stmt>
		| <block>
		| <stmt_list> <block>
	*/

    static final Set<LexicalType> FIRST = new HashSet<LexicalType>() {{
        addAll(StmtNode.FIRST); // NAME,FOR,END
        addAll(BlockNode.FIRST); // IF,DO,WHILE
    }};

    private StmtListNode(Environment env) {
        super(env, NodeType.STMT_LIST);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new StmtListNode(env);
        else return null;
    }

    // have to be loop
    @Override
    public boolean parse() throws Exception {
        while (true) {
            try {
                env.getInput().goodByeNL();

                Node handler;
                LexicalType type = env.getInput().peekUnit().getType();

                if (StmtNode.isMatch(type)) {
                    handler = StmtNode.getHandler(type, env);
                } else if (BlockNode.isMatch(type)) {
                    handler = BlockNode.getHandler(type, env);
                } else {
                    break;
                }

                parseCheck(handler, "syntax error");
                child.add(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (child.isEmpty()) return "null";
        child.stream().forEachOrdered(n -> sb.append(n + ";"));
        return new String(sb);
    }

    public Value getValue() throws Exception {
        child.stream().forEachOrdered(n -> {
            try {
                n.getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

}
