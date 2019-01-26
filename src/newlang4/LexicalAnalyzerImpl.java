package newlang4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {

    private PushbackReader reader;
    private static Map<String, LexicalUnit> reserves;
    private List<LexicalUnit> luBuffer;
    private int line;

    {
        luBuffer = new ArrayList<>();
        line = 1;
    }

    static {
        reserves = new HashMap<>();
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

    public LexicalAnalyzerImpl(InputStream in) {
        Reader rd = new InputStreamReader(in, StandardCharsets.UTF_8);
        reader = new PushbackReader(rd);
    }


    private LexicalUnit getString() throws Exception {
        String target = "";

        while (true) {
            int ci = reader.read();
            if (('a' <= ci && ci <= 'z') || ('A' <= ci && ci <= 'Z')) {
                target += (char) ci;
                continue;
            }
            if (ci > 0 && ('0' <= ci && ci <= '9')) {
                target += (char) ci;
                continue;
            }
            reader.unread(ci);
            break;
        }
        if (reserves.containsKey(target)) {
            return reserves.get(target);
        }
        return new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING));
    }

    private LexicalUnit getNum() throws Exception {
        String target = "";
        boolean floatFlug = false;

        while (true) {
            int ci = reader.read();
            if (ci >= '0' && ci <= '9') {
                target += (char) ci;
                continue;
            }
            if ((ci > 0) && (ci == '.') && !floatFlug) {
                target += (char) ci;
                floatFlug = true;
                continue;
            }
            reader.unread(ci);
            break;
        }
        if (floatFlug) {
            return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(target, ValueType.DOUBLE));
        }
        return new LexicalUnit(LexicalType.INTVAL, new ValueImpl(target, ValueType.INTEGER));
    }

    private LexicalUnit getLiteral() throws Exception {
        String target = "";
        boolean started = false;

        while (true) {
            int ci = reader.read();
            if (!started && ci == '\"') {
                started = true;
                continue;
            }
            if (started) {
                if (ci == '\"') break;
                else if (ci == -1) return new LexicalUnit(LexicalType.EOF);
                target += (char) ci;
            }
        }
        return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(target, ValueType.STRING));
    }

    private LexicalUnit getSymbol() throws Exception {
        String target = "";
        int ci = reader.read();
        target += (char) ci;

        while (reader.ready()) {
            ci = reader.read();
            if (reserves.containsKey(target + (char) ci)) {
                target += (char) ci;
            } else {
                reader.unread(ci);
                break;
            }
        }

        if (reserves.containsKey(target)) {
            LexicalUnit unit = reserves.get(target);
            if (unit.getType() == LexicalType.NL) {
                line++;
            }
            return unit;
        } else {
            return null;
        }
    }

    @Override
    public LexicalUnit get() throws Exception {
        int ci;

        // preferentially return ungeted unit
        if (!luBuffer.isEmpty()) return luBuffer.remove(luBuffer.size() - 1);

        do {
            ci = reader.read();
        } while (ci == ' ' || ci == '\t');

        if (ci == -1 || !reader.ready()) return new LexicalUnit(LexicalType.EOF);
        reader.unread(ci);

        if (('a' <= ci && ci <= 'z') || ('A' <= ci && ci <= 'Z')) {
            return getString();
        } else if ('0' <= ci && ci <= '9') {
            return getNum();
        } else if (ci == '\"') {
            return getLiteral();
        } else {
            return getSymbol();
        }
    }

    @Override
    public void unget(LexicalUnit token) throws Exception {
        if (token.getType() == LexicalType.NL) {
            line--;
        }
        luBuffer.add(token);
    }

    @Override
    public boolean expect(LexicalType type, int n) throws Exception {
        return peekUnit(n).getType() == type;
    }

    public void ungetStream(ArrayList<LexicalUnit> l) throws Exception {
        ArrayList<LexicalUnit> list = l;
        Collections.reverse(list);
        list.stream().forEachOrdered(u -> {
            try {
                unget(u);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // peek next unit
    public LexicalUnit peekUnit() throws Exception {
        LexicalUnit unit = get();
        unget(unit);
        return unit;
    }

    // peek any following unit
    public LexicalUnit peekUnit(int n) throws Exception {
        ArrayList<LexicalUnit> tmp = new ArrayList<>();
        LexicalUnit unit;
        for (int i = 0; i < n; i++) {
            tmp.add(get());
        }
        unit = tmp.get(tmp.size() - 1);
        ungetStream(tmp);
        return unit;
    }

    // good bye forever NL
    public void goodByeNL() throws Exception {
        while (expect(LexicalType.NL, 1)) {
            get();
        }
    }

    public int getLine() {
        return line;
    }
}