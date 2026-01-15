package model.expression;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap; // Added import
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;
import model.type.Type;
public class RelationalExp implements Exp {
    private final Exp e1;
    private final Exp e2;
    private final String op;

    public RelationalExp(String op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        // Pass heap
        Value v1 = e1.eval(tbl, heap);

        if (!v1.getType().equals(new IntType())) {
            throw new MyException("First operand is not an integer.");
        }

        // Pass heap
        Value v2 = e2.eval(tbl, heap);

        if (!v2.getType().equals(new IntType())) {
            throw new MyException("Second operand is not an integer.");
        }

        IntValue i1 = (IntValue) v1;
        IntValue i2 = (IntValue) v2;
        int n1 = i1.getVal();
        int n2 = i2.getVal();

        switch (op) {
            case "<": return new BoolValue(n1 < n2);
            case "<=": return new BoolValue(n1 <= n2);
            case "==": return new BoolValue(n1 == n2);
            case "!=": return new BoolValue(n1 != n2);
            case ">": return new BoolValue(n1 > n2);
            case ">=": return new BoolValue(n1 >= n2);
            default: throw new MyException("Invalid relational operator.");
        }
    }

    @Override
    public String toString() {
        return "(" + e1.toString() + " " + op + " " + e2.toString() + ")";
    }
    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = e1.typecheck(typeEnv);
        typ2 = e2.typecheck(typeEnv);

        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType();
            } else throw new MyException("Relational: second operand is not an integer");
        } else throw new MyException("Relational: first operand is not an integer");
    }
}