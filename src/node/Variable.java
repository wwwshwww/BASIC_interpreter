package node;

import newlang4.*;

public class Variable extends Node {
    private String name;
    private Value v;

    {
        type = NodeType.VARIABLE;
    }

    public Variable(LexicalUnit lu) {
        name = lu.getValue().getSValue();
    }

    public static Variable getVar(LexicalUnit lu) {
        if (lu.getType() == LexicalType.NAME) {
            return new Variable(lu);
        }
        return null;
    }

    public void setValue(Value v) {
        this.v = v;
    }

    public Value getValue() {
        return v;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

}
