package function;

import newlang4.Value;
import node.ExprListNode;

public class Print extends Function{
    public Value invoke(ExprListNode args) throws Exception {
        if(args.getExprCount() != 1){
            throw new Exception("argument error in PRINT function");
        }
        Value v = args.getExprList().get(0).getValue();
        String s;
        switch (v.getType()){
            case INTEGER:
                s = Integer.toString(v.getIValue());
                break;
            case DOUBLE:
                s = Double.toString(v.getDValue());
                break;
            case STRING:
                s = v.getSValue();
                break;
            case BOOL:
                s = Boolean.toString(v.getBValue());
                break;
            default:
                s = v.toString();
                break;
        }
        System.out.println(s);

        return null;
    }
}
