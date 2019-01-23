package node;

import newlang4.LexicalUnit;

public class BinaryNode extends Node{
    Node left, right;
    LexicalUnit operator;

    public BinaryNode(LexicalUnit operator, Node left, Node right){
        this.operator = operator;
        this.left = left;
        this.right = right;
    }


}
