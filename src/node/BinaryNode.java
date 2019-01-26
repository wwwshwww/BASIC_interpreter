package node;

import newlang4.*;

import static newlang4.LexicalType.*;
import static newlang4.ValueImpl.getNumVal;

public class BinaryNode extends Node {
    Node left, right;
    LexicalUnit operator;

    public BinaryNode(Environment env, LexicalUnit operator, Node left, Node right) {
        super(env, NodeType.BINARY);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return operator + "[" + left + ", " + right + "]";
    }

    public static boolean isNum(Value v) {
        return v.getType() == ValueType.INTEGER || v.getType() == ValueType.DOUBLE;
    }

    public static boolean isBothInt(Value v1, Value v2) {
        return v1.getType() == ValueType.INTEGER && v2.getType() == ValueType.INTEGER;
    }

    public Value getValue() throws Exception {
        updateVar(left);
        updateVar(right);
        LexicalType opeType = operator.getType();
        Value leftVal = left.getValue();
        Value rightVal = right.getValue();
        ValueType leftType = leftVal.getType();
        ValueType rightType = rightVal.getType();

        Value result;

        if ((isNum(leftVal) && !isNum(rightVal)) || (!isNum(leftVal) && isNum(rightVal))) {
            throw new Exception("can't calculate " + leftType + " and " + rightType);
        }

        // case only "string + string"
        if (leftType == ValueType.STRING && rightType == ValueType.STRING) {
            if (opeType == ADD) {
                String sVal = leftVal.getSValue() + rightVal.getSValue();
                result = new ValueImpl(sVal, ValueType.STRING);
            } else {
                throw new Exception("can't calculate string with \"" + operator + "\"");
            }
        }
        // case "num <operator> num"
        else if (isNum(leftVal) && isNum(rightVal)) {
            String rs; // result string
            ValueType rt; // result value
            double nVal;

            switch (opeType) {
                case ADD:
                    nVal = getNumVal(leftVal) + getNumVal(rightVal);
                    break;
                case SUB:
                    nVal = getNumVal(leftVal) - getNumVal(rightVal);
                    break;
                case MUL:
                    nVal = getNumVal(leftVal) * getNumVal(rightVal);
                    break;
                case DIV:
                    if (isBothInt(leftVal, rightVal)) {
                        nVal = (double) ((int) getNumVal(leftVal) / (int) getNumVal(rightVal));
                    } else {
                        nVal = getNumVal(leftVal) / getNumVal(rightVal);
                    }
                    break;
                default:
                    nVal = 0;
                    break;
            }

            if (isBothInt(leftVal, rightVal)) {
                rs = Integer.toString((int) nVal);
                rt = ValueType.INTEGER;
            } else {
                rs = Double.toString(nVal);
                rt = ValueType.DOUBLE;
            }

            result = new ValueImpl(rs, rt);
        } else {
            throw new Exception("can't calculate " + leftType + " and " + rightType);
        }

        return result;
    }
}
