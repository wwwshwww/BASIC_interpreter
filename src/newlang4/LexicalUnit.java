package newlang4;


public class LexicalUnit {
	LexicalType type;
	ValueImpl value;
	LexicalUnit link;

	public LexicalUnit(LexicalType this_type) {
	    type = this_type;
	}

	public LexicalUnit(LexicalType this_type, ValueImpl this_value) {
		type = this_type;
		value = this_value;
	}

	public Value getValue() {
		return value;
	}

	public LexicalType getType() {
		return type;
	}

	public String toString() {
		switch(type) {
	    case LITERAL:
            return value.getSValue();
	    case NAME:
	    	return value.getSValue();
	    case DOUBLEVAL:
	    	return Double.toString(value.getDValue());
	    case INTVAL:
	    	return Integer.toString(value.getIValue());
	    case IF:
            return ("IF");
	    case THEN:
            return ("THEN");
	    case ELSE:
            return ("ELSE");
	    case FOR:
            return ("FOR");
	    case FORALL:
            return ("FORALL");
	    case NEXT:
            return ("NEXT");
	    case SUB:
            return ("-");
	    case DIM:
            return ("DIM");
	    case AS:
            return ("AS");
	    case END:
            return ("END");
	    case EOF:
            return ("EOF");
	    case NL:
            return ("NL");
	    case EQ:
            return ("=");
	    case LT:
	    	return ("<");
	    case GT:
	    	return (">");
	    case LE:
	    	return ("<=");
	    case GE:
	    	return (">=");
	    case DOT:
	    	return (".");
	    case WHILE:
	    	return ("WHILE");
	    case UNTIL:
	    	return ("UNTIL");
	    case ADD:
	    	return ("+");
	    case MUL:
	    	return ("*");
	    case DIV:
	    	return ("/");
	    case LP:
	    	return ("LP");
	    case RP:
	    	return ("RP");
	    case COMMA:
	    	return ("COMMA");
	    case LOOP:
	    	return ("LOOP");
	    case TO:
	    	return ("TO");
	    case WEND:
	    	return ("WEND");
	    case ELSEIF:
	    	return ("ELSEIF");
	    case NE:
	    	return ("<>");
	    case ENDIF:
	    	return ("ENDIF");
	    case DO:
	    	return ("DO");
	    }
	    return "";
	}
}
