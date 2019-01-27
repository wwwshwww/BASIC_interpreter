package node;

import analyzer.Environment;
import analyzer.LexicalType;
import analyzer.NodeType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EndNode extends Node {

    static final Set<LexicalType> FIRST = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.END
    ));

    private EndNode(Environment env) {
        super(env, NodeType.END);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new EndNode(env);
        else return null;
    }

    public boolean parse() throws Exception {
        env.getInput().get();
        return true;
    }

    public String toString() {
        return "END";
    }
}
