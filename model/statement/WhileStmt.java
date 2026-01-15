package model.statement;

import exception.MyException;
import model.PrgState;
import model.expression.Exp;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;
import model.adt.MyIDictionary;
import model.type.Type;

public class WhileStmt implements IStmt {
    private Exp exp;
    private IStmt stmt;

    public WhileStmt(Exp exp, IStmt stmt) {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value val = exp.eval(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new BoolType())) {
            throw new MyException("While condition is not a boolean.");
        }

        if (((BoolValue) val).getVal()) {
            // Push the while statement back onto the stack, then the body
            state.getStk().push(this);
            state.getStk().push(stmt);
        }
        // If false, do nothing (statement effectively removed from stack)
        return state;
    }

    @Override
    public String toString() {
        return "while(" + exp.toString() + ") " + stmt.toString();
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            stmt.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The condition of WHILE has not the type bool");
    }
}