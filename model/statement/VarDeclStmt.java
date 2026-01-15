package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.type.Type;
import model.value.Value;

public class VarDeclStmt implements IStmt {
    private final String name;
    private final Type typ;

    public VarDeclStmt(String name, Type typ) {
        this.name = name;
        this.typ = typ;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();

        if (symTbl.isDefined(name)) {
            throw new MyException("Variable " + name + " is already declared.");
        }

        // MODIFICAT: Folosim defaultValue()
        Value defaultValue = typ.defaultValue();
        symTbl.put(name, defaultValue);

        return state;
    }

    @Override
    public String toString() {
        return typ.toString() + " " + name;
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.put(name, typ);
        return typeEnv;
    }
}