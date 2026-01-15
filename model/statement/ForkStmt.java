package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.adt.MyStack;
import model.type.Type;

public class ForkStmt implements IStmt {
    private IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // 1. Creăm o stivă nouă pentru noul thread
        MyIStack<IStmt> newStack = new MyStack<>();

        // 2. Clonăm tabela de simboluri (Deep Copy) - restul sunt partajate
        // Parametrul pentru constructorul PrgState este 'this.stmt' (instrucțiunea din fork)
        return new PrgState(
                newStack,
                state.getSymTable().deepCopy(),
                state.getOut(),
                state.getFileTable(),
                state.getHeap(),
                stmt
        );
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Fork creează un thread nou, deci verificăm corpul cu o copie a mediului
        stmt.typecheck(typeEnv.deepCopy());
        // Fork nu modifică mediul părintelui
        return typeEnv;
    }
}