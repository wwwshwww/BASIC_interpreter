package newlang4;

import function.*;
import node.*;

import java.util.HashMap;

public class Environment {
    private LexicalAnalyzerImpl input;

    private HashMap<String, Variable> var_table = new HashMap<>();
    private static HashMap<String, Function> func_table = new HashMap<>();

    static {
        func_table.put("PRINT", new Print());
        func_table.put("SQRT", new Sqrt());
        func_table.put("MAX", new Max());
        func_table.put("MIN", new Min());
    }

    public Environment(LexicalAnalyzerImpl input) {
        this.input = input;
    }

    public LexicalAnalyzerImpl getInput() {
        return input;
    }

    public void entryVar(Variable v) {
        var_table.put(v.getName(), v);
    }

    public boolean isVarExist(String s) {
        return var_table.containsKey(s);
    }

    public boolean isFuncExist(String s) {
        return func_table.containsKey(s);
    }

    public Function getFunction(String s) {
        if (isFuncExist(s)) return func_table.get(s);
        else return null;
    }

    public Variable getVariable(String s) {
        if (isVarExist(s)) return var_table.get(s);
        else return null;
    }
}
