package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIList;
import model.expression.Exp;
import model.value.Value;
import model.adt.MyIDictionary;
import model.type.Type;

public class PrintStmt implements IStmt {
    private final Exp exp;

    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<Value> out = state.getOut();

        // --- FIX AICI: Adăugat state.getHeap() ---
        Value val = exp.eval(state.getSymTable(), state.getHeap());

        out.add(val);
        return state;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }
}