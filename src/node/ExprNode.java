package node;

import newlang4.*;

import java.util.*;

public class ExprNode extends Node {

    LexicalUnit value = null;
    Node bin = null;
    boolean single = false;

    static final Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.SUB,
            LexicalType.LP,
            LexicalType.NAME,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL
    ));

    static final Map<LexicalType, Integer> OPERATOR = new HashMap<>();

    static {
        OPERATOR.put(LexicalType.ADD, 1);
        OPERATOR.put(LexicalType.SUB, 2);
        OPERATOR.put(LexicalType.MUL, 3);
        OPERATOR.put(LexicalType.DIV, 4);
    }

    private ExprNode(Environment env) {
        super(env, NodeType.EXPR);
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception {
        if (isMatch(type)) return new ExprNode(env);
        else return null;
    }

    public Node getOperand() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        LexicalUnit firstUnit = la.peekUnit();
        LexicalType firstType = firstUnit.getType();

        Node operand = null;

        // case "- <operand>" -> "-1 * <operand>"
        if (firstType == LexicalType.SUB) {
            ArrayList<LexicalUnit> ungetList = new ArrayList<>();

            ValueImpl val = new ValueImpl("-1", ValueType.INTEGER);
            LexicalUnit minusUnit = new LexicalUnit(LexicalType.INTVAL, val);
            LexicalUnit operator = new LexicalUnit(LexicalType.MUL);

            la.get(); // execute "-"
            ungetList.add(minusUnit); // -1
            ungetList.add(operator); // *
            la.ungetStream(ungetList);

            operand = getOperand();
        }
        // "<name>..."
        else if (firstType == LexicalType.NAME) {
            // case "func(<expr>, ...)"
            if (la.expect(LexicalType.LP, 2)) {
                operand = CallFuncNode.getHandler(firstType, env);
                operand.parse();
            }
            // case "<variable><operator>"
            else {
                String varName = la.get().getValue().getSValue();
                operand = env.getVariable(varName);
                if(operand == null) {
                    throw new Exception("undefined variable : " + varName + " in " + la.getLine());
                }
            }
        }
        // case "(<expr>)"
        else if (firstType == LexicalType.LP) {
            la.get(); // execute "("
            operand = ExprNode.getHandler(la.peekUnit().getType(), env);
            operand.parse();
            la.get(); // execute ")"
        }
        // case "<value><operator>"
        else {
            operand = ExprNode.getHandler(la.peekUnit().getType(), env);
            ((ExprNode) operand).single = true;
            operand.parse();
        }

        return operand;

    }

    public LexicalUnit getOperator() throws Exception {
        LexicalUnit operator = null;
        if (OPERATOR.containsKey(env.getInput().peekUnit().getType())) {
            operator = env.getInput().get();
        }
        return operator;
    }

    public boolean parse() throws Exception {
        LexicalAnalyzerImpl la = env.getInput();
        LexicalUnit unit = la.peekUnit();

        List<Node> operands = new ArrayList<>();
        List<LexicalUnit> operators = new ArrayList<>();

        // ex: (1)
        if (single) {
            value = unit;
            return true;
        }

        // (1 + 2 / 2) -> (1,2,2), (+,/)
        while (ExprNode.isMatch(unit.getType())) {
            Node left = getOperand();
            if (left == null) {
                throw new Exception("invalid expression : " + la.getLine());
            }
            operands.add(left);

            LexicalUnit operator = getOperator();
            if (operator == null) {
                break;
            }
            operators.add(operator);

            unit = la.peekUnit();
        }

        // (1 + 2 / 2) -> (1 + (bin)) -> (((bin)))
        // ; (1,2,2), (+,/) -> (1,(bin)), (+) -> (((bin))), ()
        while (!operators.isEmpty()) {
            int max = 0;
            int coo = 0;
            for (int i = 0; i < operators.size(); i++) {
                LexicalUnit tor = operators.get(i);
                int val = OPERATOR.get(tor);
                if (val > max) {
                    max = val;
                    coo = i;
                }
            }

            Node right = operands.remove(coo + 1);
            Node left = operands.remove(coo);
            LexicalUnit operator = operators.remove(coo);

            Node bin = new BinaryNode(operator, left, right);
            operands.add(coo, bin);
        }

        bin = operands.get(0);

        return true;

    }

}
