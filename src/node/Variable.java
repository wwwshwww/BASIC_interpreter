package node;

import newlang4.Environment;
import newlang4.LexicalType;
import newlang4.LexicalUnit;
import newlang4.Value;

public class Variable extends Node {
	private String name;
	private Value v;

	/** Creates a new instance of variable */
	public Variable(String name) {
		this.name = name;
	}

	public Variable(LexicalUnit lu) {
		name = lu.getValue().getSValue();
	}

	public static Variable getVar(Environment env, LexicalUnit lu) {

		/*
		 * String s = first.getValue().getSValue();
		 * v = my_env.getVariable(s);
		 * return v;
		 */

		if (lu.getType() == LexicalType.NAME) {
			String name = lu.getValue().getSValue();
			Variable var;

			if(env.isEntriedVar(name)){
				var = env.getVariable(lu.getValue().getSValue());
			}else{
				var = new Variable(name);
			}

			return var;
		}
		return null;
	}

	public void setValue(Value v) {
		this.v = v;
	}

	public Value getValue() {
		return v;
	}

	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}

}
