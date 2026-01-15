package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.type.Type;
public class CompStmt implements IStmt {
    private final IStmt first;
    private final IStmt snd;

    public CompStmt(IStmt first, IStmt snd) {
        this.first = first;
        this.snd = snd;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + snd.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        stk.push(snd);
        stk.push(first);
        return state;
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Efectul primei instrucțiuni se propagă la a doua
        return snd.typecheck(first.typecheck(typeEnv));
    }

}