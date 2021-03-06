package analyzer;

public class ValueImpl implements Value {

    ValueType type;
    int ivalue;
    double dvalue;
    String svalue;
    boolean bvalue;

    public ValueImpl(String src, ValueType targetType) {
        this.type = targetType;
        switch (targetType) {
            case INTEGER:
                ivalue = Integer.parseInt(src);
                break;
            case DOUBLE:
                dvalue = Double.parseDouble(src);
                break;
            case STRING:
                svalue = src;
                break;
            case BOOL:
                bvalue = Boolean.valueOf(src);
                break;
        }
    }

    @Override
    public String getSValue() {
        return svalue;
    }

    @Override
    public int getIValue() {
        return ivalue;
    }

    @Override
    public double getDValue() {
        return dvalue;
    }

    @Override
    public boolean getBValue() {
        return bvalue;
    }

    @Override
    public ValueType getType() {
        return type;
    }

    public static boolean isNum(Value v) {
        return v.getType() == ValueType.INTEGER || v.getType() == ValueType.DOUBLE;
    }

    public static boolean isBothInt(Value v1, Value v2) {
        return v1.getType() == ValueType.INTEGER && v2.getType() == ValueType.INTEGER;
    }

    public static double getNumVal(Value v) throws Exception {
        switch (v.getType()) {
            case INTEGER:
                return (double) v.getIValue();
            case DOUBLE:
                return v.getDValue();
            default:
                throw new Exception(v + " is not numerical"); // meaningless
        }
    }

}
