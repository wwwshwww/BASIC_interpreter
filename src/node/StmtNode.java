package node;

import newlang4.Environment;
import newlang4.LexicalType;
import newlang4.NodeType;

import java.util.*;

public class StmtNode extends Node {

	/*
	<stmt> ::=
			<subst>
			| <call_sub>
			| <call_func>
			| <FOR> <subst> <TO> <INTVAL> <NL> <stmt_list> <NEXT> <NAME>
			| <END>
	*/
	
//	LexicalType.NAME,
//	LexicalType.FOR,
//	LexicalType.END
	
	Node body;
	static final Set<LexicalType> FIRST = new HashSet<LexicalType>() {{
		addAll(SubstNode.FIRST);
		addAll(CallFuncNode.FIRST);
		addAll(ForStmtNode.FIRST);
		addAll(EndNode.FIRST);
	}};

	private StmtNode(Environment env) {
		super(env, NodeType.STMT);
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(LexicalType type, Environment env) throws Exception{
		LexicalType firstType = env.getInput().peekUnit().getType();
		LexicalType secondType = env.getInput().peekUnit(2).getType();

		switch(firstType) {
			case NAME:
				// <leftvar> <EQ> <expr>
				if(secondType == LexicalType.EQ) {
					return SubstNode.getHandler(firstType, env);
				}
				// <NAME> <expr_list>
				else if(ExprListNode.isMatch(secondType)) {
					return CallFuncNode.getHandler(firstType, env);
				}
				else{
					throw new Exception("syntax error : invalid statement");
				}
			case FOR:
				return ForStmtNode.getHandler(firstType, env);
			case END:
				return EndNode.getHandler(firstType,env);
		}
		return null;
	}
	
}