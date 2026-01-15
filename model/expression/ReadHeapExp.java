package model.expression;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class ReadHeapExp implements Exp {
    private Exp exp;

    public ReadHeapExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        Value val = exp.eval(tbl, heap);
        if (!(val instanceof RefValue)) {
            throw new MyException("Expression is not a RefValue.");
        }

        RefValue refVal = (RefValue) val;
        int address = refVal.getAddr();

        if (!heap.containsKey(address)) {
            throw new MyException("Address " + address + " is not defined in the heap.");
        }

        return heap.get(address);
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else
            throw new MyException("The rH argument is not a Ref Type");
    }
}