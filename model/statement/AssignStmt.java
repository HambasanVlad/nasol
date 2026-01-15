package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.Type;
import model.value.Value;

public class AssignStmt implements IStmt {
    private final String id;
    private final Exp exp;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();

        if (symTbl.isDefined(id)) {
            // --- FIX AICI: Adăugat state.getHeap() ---
            Value val = exp.eval(symTbl, state.getHeap());

            Type typId = (symTbl.lookup(id)).getType();

            if (val.getType().equals(typId)) {
                symTbl.update(id, val);
            } else {
                throw new MyException("Declared type of variable " + id +
                        " and type of the assigned expression do not match.");
            }
        } else {
            throw new MyException("The used variable " + id + " was not declared before.");
        }

        return state;
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);

        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types");
    }
}