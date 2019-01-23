package node;

import newlang4.Environment;
import newlang4.LexicalType;
import newlang4.NodeType;
import newlang4.Value;

public class Node {
	NodeType type;
	Environment env;

	/** Creates a new instance of Node */
	public Node() {
	}

	public Node(NodeType type) {
		this.type = type;
	}

	public Node(Environment env) {
		this.env = env;
	}

	public Node(Environment env, NodeType type) {
		this.type = type;
		this.env = env;
	}

	public NodeType getType() {
		return type;
	}

	public boolean parse() throws Exception {
		return true;
	}

	public Value getValue() throws Exception {
		return null;
	}

	public String toString() {
		if (type == NodeType.END) return "END";
		else return "Node";
	}

	// throw exception at case next unit isn't expected unit
	public void nextCheck(LexicalType type, String error_message) throws Exception{
		if(!env.getInput().expect(type, 1)){
			throw new Exception(error_message + " : " + env.getInput().getLine());
		}
	}

	// throw exception at case of node is null or to parse is fail
	public void parseCheck(Node node, String error_message) throws Exception{
		if(node == null || !node.parse()){
			throw new Exception(error_message +" : " + env.getInput().getLine());
		}
	}

}
