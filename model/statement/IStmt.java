package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.type.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;

    // --- A6: Type Check ---
    MyIDictionary<String,Type> typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
}