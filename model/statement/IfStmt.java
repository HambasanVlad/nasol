package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.expression.Exp;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;
import model.adt.MyIDictionary;
import model.type.Type;

public class IfStmt implements IStmt {
    private final Exp exp;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(Exp e, IStmt t, IStmt el) {
        this.exp = e;
        this.thenS = t;
        this.elseS = el;
    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ") THEN(" + thenS.toString() + ")ELSE(" + elseS.toString() + "))";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();

        // --- FIX AICI: Adăugat state.getHeap() ---
        Value cond = exp.eval(state.getSymTable(), state.getHeap());

        if (!cond.getType().equals(new BoolType())) {
            throw new MyException("Conditional expression is not a boolean.");
        }

        BoolValue boolCond = (BoolValue) cond;
        if (boolCond.getVal()) {
            stk.push(thenS);
        } else {
            stk.push(elseS);
        }

        return state;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            // Verificăm ramurile cu COPII ale mediului, pentru ca variabilele declarate in IF să nu iasă afară
            thenS.typecheck(typeEnv.deepCopy());
            elseS.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The condition of IF has not the type bool");
    }
}