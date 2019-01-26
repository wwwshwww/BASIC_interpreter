package function;

import newlang4.Value;
import newlang4.ValueImpl;
import node.ExprListNode;

import static newlang4.ValueImpl.*;

public class Min extends Function {
    public Value invoke(ExprListNode args) throws Exception {
        if (args.getExprCount() != 2) {
            throw new Exception("argument count error in MAX function");
        }

        Value v1 = args.getExprList().get(0).getValue();
        Value v2 = args.getExprList().get(1).getValue();

        if (isNum(v1) && isNum(v2)) {
            double b1 = getNumVal(v1);
            double b2 = getNumVal(v2);
            if (b1 > b2) return v2;
            else return v1;
        } else {
            throw new Exception("argument is not numerical");
        }
    }
}
