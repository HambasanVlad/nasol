package model.expression;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap; // Add Import
import model.value.Value;
import model.type.Type;

public interface Exp {
    Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Integer,Value> heap) throws MyException;

    // --- A6: Type Check ---
    Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
}