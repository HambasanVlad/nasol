package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class WriteHeapStmt implements IStmt {
    private String varName;
    private Exp exp;

    public WriteHeapStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!state.getSymTable().isDefined(varName)) {
            throw new MyException("Variable " + varName + " not defined.");
        }

        Value val = state.getSymTable().lookup(varName);
        if (!(val.getType() instanceof RefType)) {
            throw new MyException("Variable " + varName + " is not a RefType.");
        }

        RefValue refVal = (RefValue) val;
        if (!state.getHeap().containsKey(refVal.getAddr())) {
            throw new MyException("Address " + refVal.getAddr() + " is not in the heap.");
        }

        Value evaluated = exp.eval(state.getSymTable(), state.getHeap());
        if (!evaluated.getType().equals(((RefType) refVal.getType()).getInner())) {
            throw new MyException("Type mismatch for heap write.");
        }

        state.getHeap().update(refVal.getAddr(), evaluated);
        return state;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = exp.typecheck(typeEnv);

        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("WriteHeap: right hand side and left hand side have different types");
    }
}