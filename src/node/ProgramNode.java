package node;

import newlang4.Environment;
import newlang4.LexicalType;
import newlang4.LexicalUnit;
import newlang4.NodeType;

import java.util.*;

public class ProgramNode extends Node {

	/*
	LexicalType.NAME
	LexicalType.FOR
	LexicalType.END
	LexicalType.IF
	LexicalType.WHILE
	LexicalType.DO
	*/
	static Set<LexicalType> FIRST = new HashSet<LexicalType>(){{
		addAll(StmtListNode.FIRST);
	}};
	
	private ProgramNode(Environment e) {
		super(e);
		type = NodeType.PROGRAM;
	}
	
	public static Node getHandler(LexicalType type, Environment env) {
		return StmtListNode.getHandler(type, env);
	}
	
	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}
	
	public static Node getMatch(LexicalUnit lu) {
		return null;
	}
	
	public String toString() {
		if (type == NodeType.END) return "END";
		else return "Node";
	}
}
