package newlang4;

public interface LexicalAnalyzer {
    public LexicalUnit get() throws Exception;

    public boolean expect(LexicalType type, int n) throws Exception;

    public void unget(LexicalUnit token) throws Exception;
}
