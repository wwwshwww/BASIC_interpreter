package newlang4;

import node.*;

import java.util.HashMap;

public class Environment {
    private LexicalAnalyzerImpl input;

    private HashMap<String, Variable> var_table = new HashMap<>();
    private HashMap<String, Function> func_table = new HashMap<>();

    public Environment(LexicalAnalyzerImpl my_input) {
        input = my_input;
    }

    public LexicalAnalyzerImpl getInput() {
        return input;
    }

    public void entryVar(Variable v){
        var_table.put(v.getName(), v);
    }

    public boolean isEntriedVar(String s){
        return var_table.containsKey(s);
    }

    public Variable getVariable(String s){
        if(isEntriedVar(s)) return var_table.get(s);
        else return null;
    }
}
