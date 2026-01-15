package model.expression;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap; // Added import
import model.type.IntType;
import model.value.IntValue;
import model.value.Value;
import model.type.Type;
public class ArithExp implements Exp {
    private final Exp e1;
    private final Exp e2;
    private final int op; // 1-plus, 2-minus, 3-star, 4-divide

    public ArithExp(char op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        if (op == '+') this.op = 1;
        else if (op == '-') this.op = 2;
        else if (op == '*') this.op = 3;
        else if (op == '/') this.op = 4;
        else this.op = 0;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        Value v1, v2;
        // Pass heap to recursive calls
        v1 = e1.eval(tbl, heap);

        if (!v1.getType().equals(new IntType())) {
            throw new MyException("First operand is not an integer");
        }

        // Pass heap to recursive calls
        v2 = e2.eval(tbl, heap);

        if (!v2.getType().equals(new IntType())) {
            throw new MyException("Second operand is not an integer");
        }

        IntValue i1 = (IntValue) v1;
        IntValue i2 = (IntValue) v2;
        int n1 = i1.getVal();
        int n2 = i2.getVal();

        switch (op) {
            case 1: return new IntValue(n1 + n2);
            case 2: return new IntValue(n1 - n2);
            case 3: return new IntValue(n1 * n2);
            case 4:
                if (n2 == 0) throw new MyException("Division by zero");
                else return new IntValue(n1 / n2);
            default: throw new MyException("Invalid arithmetic operation");
        }
    }

    @Override
    public String toString() {
        String opStr = "";
        if (op == 1) opStr = "+";
        else if (op == 2) opStr = "-";
        else if (op == 3) opStr = "*";
        else if (op == 4) opStr = "/";
        return "(" + e1.toString() + " " + opStr + " " + e2.toString() + ")";
    }
    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = e1.typecheck(typeEnv);
        typ2 = e2.typecheck(typeEnv);

        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else
                throw new MyException("Arithmetic: second operand is not an integer");
        } else
            throw new MyException("Arithmetic: first operand is not an integer");
    }
}