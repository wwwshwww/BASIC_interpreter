package newlang4;

public class ValueImpl implements Value {
	
	ValueType type;
	int ivalue;
	double dvalue;
	String svalue;
	boolean bvalue;
	
	public ValueImpl(String src, ValueType targetType) {
		this.type = targetType;
		switch(targetType) {
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
}
