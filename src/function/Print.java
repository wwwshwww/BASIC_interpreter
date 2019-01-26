package function;

import newlang4.Value;
import node.ExprListNode;

public class Print extends Function{
    public Value invoke(ExprListNode args) throws Exception {
        if(args.getExprCount() != 1){
            throw new Exception("argument error in PRINT function");
        }
        System.out.println(args.getExprList().get(0));
        return null;
    }
}
