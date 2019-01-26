package function;

import newlang4.Value;
import newlang4.ValueImpl;
import newlang4.ValueType;
import node.ExprListNode;

import static newlang4.ValueImpl.*;

public class Sqrt extends Function {
    public Value invoke(ExprListNode args) throws Exception {
        if (args.getExprCount() != 1) {
            throw new Exception("argument count error in SQRT function");
        }

        Value v = args.getExprList().get(0).getValue();
        if (isNum(v)) {
            double val = getNumVal(v);
            val = Math.sqrt(val);
            return new ValueImpl(Double.toString(val), ValueType.DOUBLE);
        } else {
            throw new Exception("argument \"" + v + "\" is not numerical");
        }
    }
}
