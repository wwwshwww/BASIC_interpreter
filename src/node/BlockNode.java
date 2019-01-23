package node;
import newlang4.Environment;
import newlang4.LexicalType;

import java.util.*;

public class BlockNode extends Node {

	public static final Set<LexicalType> FIRST = new HashSet<LexicalType>(){{
		addAll(IfBlockNode.FIRST);
		addAll(WhileBlockNode.FIRST);
		addAll(DoBlockNode.FIRST);
	}};
	
	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}
	
	public static Node getHandler(LexicalType type, Environment env) {
		if(IfBlockNode.isMatch(type)) return IfBlockNode.getHandler(type, env);
		if(WhileBlockNode.isMatch(type)) return WhileBlockNode.getHandler(type, env);
		if(DoBlockNode.isMatch(type)) return DoBlockNode.getHandler(type, env);
		return null;
	}

}
