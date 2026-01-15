package model.expression;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap; // Added import
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;
import model.type.Type;
public class LogicExp implements Exp {
    private final Exp e1;
    private final Exp e2;
    private final int op; // 1-and, 2-or

    public LogicExp(int op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        Value v1, v2;
        // Pass heap
        v1 = e1.eval(tbl, heap);

        if (!v1.getType().equals(new BoolType())) {
            throw new MyException("First operand is not a boolean");
        }

        // Pass heap
        v2 = e2.eval(tbl, heap);

        if (!v2.getType().equals(new BoolType())) {
            throw new MyException("Second operand is not a boolean");
        }

        BoolValue b1 = (BoolValue) v1;
        BoolValue b2 = (BoolValue) v2;
        boolean n1 = b1.getVal();
        boolean n2 = b2.getVal();

        switch (op) {
            case 1: return new BoolValue(n1 && n2);
            case 2: return new BoolValue(n1 || n2);
            default: throw new MyException("Invalid logical operation");
        }
    }

    @Override
    public String toString() {
        String opStr = (op == 1) ? "and" : "or";
        return "(" + e1.toString() + " " + opStr + " " + e2.toString() + ")";
    }
    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = e1.typecheck(typeEnv);
        typ2 = e2.typecheck(typeEnv);

        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else throw new MyException("Logic: second operand is not a boolean");
        } else throw new MyException("Logic: first operand is not a boolean");
    }
}