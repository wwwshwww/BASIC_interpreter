package newlang3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LexicalAnalyzerImpl implements LexicalAnalyzer{

	PushbackReader reader;
	static Map<String,LexicalUnit> reserves = new HashMap<>();
	
	public LexicalAnalyzerImpl(InputStream in) {
		Reader rd = new InputStreamReader(in, StandardCharsets.UTF_8);
		reader = new PushbackReader(rd);
	}
	
	static {
		reserves.put("IF", new LexicalUnit(LexicalType.IF));
		reserves.put("THEN", new LexicalUnit(LexicalType.THEN));
		reserves.put("ELSE", new LexicalUnit(LexicalType.ELSE));
		reserves.put("ELSEIF", new LexicalUnit(LexicalType.ELSEIF));
		reserves.put("ENDIF", new LexicalUnit(LexicalType.ENDIF));
		reserves.put("FOR", new LexicalUnit(LexicalType.FOR));
		reserves.put("FORALL", new LexicalUnit(LexicalType.FORALL));
		reserves.put("NEXT", new LexicalUnit(LexicalType.NEXT));
		reserves.put("FUNC", new LexicalUnit(LexicalType.FUNC));
		reserves.put("DIM", new LexicalUnit(LexicalType.DIM));
		reserves.put("AS", new LexicalUnit(LexicalType.AS));
		reserves.put("END", new LexicalUnit(LexicalType.END));
		reserves.put("WHILE", new LexicalUnit(LexicalType.WHILE));
		reserves.put("DO", new LexicalUnit(LexicalType.DO));
		reserves.put("UNTIL", new LexicalUnit(LexicalType.UNTIL));
		reserves.put("LOOP", new LexicalUnit(LexicalType.LOOP));
		reserves.put("TO", new LexicalUnit(LexicalType.TO));
		reserves.put("WEND", new LexicalUnit(LexicalType.WEND));
		reserves.put("=", new LexicalUnit(LexicalType.EQ));
		reserves.put("<", new LexicalUnit(LexicalType.LT));
		reserves.put(">", new LexicalUnit(LexicalType.GT));
		reserves.put("<=", new LexicalUnit(LexicalType.LE));
		reserves.put("=<", new LexicalUnit(LexicalType.LE));
		reserves.put(">=", new LexicalUnit(LexicalType.GE));
		reserves.put("=>", new LexicalUnit(LexicalType.GE));
		reserves.put("<>", new LexicalUnit(LexicalType.NE));
		reserves.put("\n", new LexicalUnit(LexicalType.NL));
		reserves.put(".", new LexicalUnit(LexicalType.DOT));
		reserves.put("+", new LexicalUnit(LexicalType.ADD));
		reserves.put("-", new LexicalUnit(LexicalType.SUB));
		reserves.put("*", new LexicalUnit(LexicalType.MUL));
		reserves.put("/", new LexicalUnit(LexicalType.DIV));
		reserves.put("(", new LexicalUnit(LexicalType.LP));
		reserves.put(")", new LexicalUnit(LexicalType.RP));
		reserves.put(",", new LexicalUnit(LexicalType.COMMA));
	}
	
	private LexicalUnit getString() throws Exception {
		String target = "";
		
		while(true) {
			int ci = reader.read();
			char c = (char)ci;
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				target += c;
				continue;
			}
			if (ci > 0 && (c >= '0' && c <= '9')) {
				target += c;
				continue;
			}
			reader.unread(ci);
			break;
		}
		if(reserves.containsKey(target)) {
			return reserves.get(target);
		}
		return new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING));
	}
	
	private LexicalUnit getNum() throws Exception {
		String target = "";
		boolean floatFlug = false;
		
		while(true) {
			int ci = reader.read();
			char c = (char)ci;
			if (c >= '0' && c <= '9') {
				target += c;
				continue;
			}
			if((ci > 0) && (c == '.') && !floatFlug) {
				target += c;
				floatFlug = true;
				continue;
			}
			reader.unread(ci);
			break;
		}
		if(floatFlug) {
			return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(target, ValueType.DOUBLE));
		}
		return new LexicalUnit(LexicalType.INTVAL, new ValueImpl(target, ValueType.INTEGER));
	}
	
	private LexicalUnit getLiteral() throws Exception {
		String target = "";
		boolean started = false;
		
		while(true) {
			int ci = reader.read();
			char c = (char)ci;
			if (!started && c == '\"') {
				started = true;
				continue;
			}
			if (started) {
				if(c == '\"') {
					break;
				}
				target += c;
			}
		}
		return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(target, ValueType.STRING));
	}
	
	private LexicalUnit getSymbol() throws Exception {
		String target = "";
		
		int ci = reader.read();
		char c = (char)ci;
		target += c;
		
		if(!reader.ready()) return new LexicalUnit(LexicalType.EOF);
		
		if(c == '=') {
			ci = reader.read();
			c = (char)ci;
			if(c == '<' || c == '>') target += c;
			else reader.unread(ci);
		}else if(c == '<') {
			ci = reader.read();
			c = (char)ci;
			if(c == '=') target += c;
			else reader.unread(ci);
		}else if(c == '>') {
			ci = reader.read();
			c = (char)ci;
			if(c == '=') target += c;
			else reader.unread(ci);
		}
		
		if(reserves.containsKey(target)){
			return reserves.get(target);
		}else {
			return null;
		}
	}
	
	@Override
	public LexicalUnit get() throws Exception {
		int ci;
		char c;
		do {
			ci = reader.read();
			c = (char)ci;
		}while(c == ' ' || c == '\t');
		reader.unread(ci);
		
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
			return getString();
		}else if(c >= '0' && c <= '9') {
			return getNum();
		}else if(c == '\"') {
			return getLiteral();
		}else {
			return getSymbol();
		}
	}

	@Override
	public boolean expect(LexicalType type) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unget(LexicalUnit token) throws Exception {
		// TODO Auto-generated method stub	
	}
}