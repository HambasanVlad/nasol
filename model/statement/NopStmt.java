package model.statement;

import exception.MyException;
import model.PrgState;

public class NopStmt implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        // Does nothing
        return state;
    }

    @Override
    public String toString() {
        return "nop";
    }
    @Override
    public model.adt.MyIDictionary<String, model.type.Type> typecheck(model.adt.MyIDictionary<String, model.type.Type> typeEnv) throws MyException {
        return typeEnv;
    }
}