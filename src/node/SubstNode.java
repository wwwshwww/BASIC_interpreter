package node;

import newlang4.Environment;
import newlang4.LexicalType;
import newlang4.NodeType;

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
		if(SubstNode.isMatch(type) && env.getInput().expect(LexicalType.EQ, 2)){
			return new SubstNode(env);
		}
		return null;
	}
	
	public boolean parse() throws Exception {
		leftvar = Variable.getVar(env, env.getInput().get());
		env.getInput().get(); // execute "="
		expr = ExprNode.getHandler(env.getInput().peekUnit().getType(), env);

//		env.entryVar(leftvar, expr.parse());

		parseCheck(expr, "invalid expression");
//		leftvar.setValue(expr.getValue());

		return true;
	}

	public String getVarName(){
		return leftvar.getName();
	}
}
