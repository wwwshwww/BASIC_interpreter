package node;

import analyzer.Environment;
import analyzer.LexicalType;
import analyzer.NodeType;
import analyzer.Value;

import java.util.*;

public class SubstNode extends Node {

	/*
	<subst> ::=
			<leftvar> <EQ> <expr>
	*/

    private Node expr;
    private Variable leftvar;

    static final Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.NAME
    ));

    private SubstNode(Environment env) {
        super(env, NodeType.ASSIGN_STMT);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception {
        if (SubstNode.isMatch(type) && env.getInput().expect(LexicalType.EQ, 2)) {
            return new SubstNode(env);
        }
        return null;
    }

    public boolean parse() throws Exception {
        leftvar = Variable.getVar(env.getInput().get());
        env.getInput().get(); // execute "="
        expr = ExprNode.getHandler(env.getInput().peekUnit().getType(), env);

        parseCheck(expr, "invalid expression");

        return true;
    }

    public String getVarName() {
        return leftvar.getName();
    }

    public String toString() {
        return getVarName() + "[" + expr + "]";
    }

    public Value getValue() throws Exception {
        leftvar.setValue(expr.getValue());
        env.entryVar(leftvar);
        return null;
    }
}
