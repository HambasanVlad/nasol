package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class NewStmt implements IStmt {
    private String varName;
    private Exp exp;

    public NewStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!state.getSymTable().isDefined(varName)) {
            throw new MyException("Variable " + varName + " is not defined.");
        }

        Value varValue = state.getSymTable().lookup(varName);
        if (!(varValue.getType() instanceof RefType)) {
            throw new MyException("Variable " + varName + " is not of RefType.");
        }

        Value evaluated = exp.eval(state.getSymTable(), state.getHeap());
        Type locationType = ((RefType) varValue.getType()).getInner();

        if (!evaluated.getType().equals(locationType)) {
            throw new MyException("Type mismatch: " + varName + " references " + locationType + " but expression is " + evaluated.getType());
        }

        int newAddress = state.getHeap().allocate(evaluated);
        state.getSymTable().update(varName, new RefValue(newAddress, locationType));

        return state;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp.toString() + ")";
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = exp.typecheck(typeEnv);

        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("NEW stmt: right hand side and left hand side have different types");
    }
}