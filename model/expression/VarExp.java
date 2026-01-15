package model.expression;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap; // Added import
import model.value.Value;
import model.type.Type;
public class VarExp implements Exp {
    private final String id;

    public VarExp(String id) {
        this.id = id;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        return tbl.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }
    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}