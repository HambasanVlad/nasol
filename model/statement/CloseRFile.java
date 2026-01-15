package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.StringType;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFile implements IStmt {
    private final Exp exp;

    public CloseRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        // --- FIX AICI: Adăugat state.getHeap() ---
        Value val = exp.eval(symTbl, state.getHeap());

        if (!val.getType().equals(new StringType())) {
            throw new MyException("File path expression did not evaluate to a string.");
        }

        StringValue fileName = (StringValue) val;
        if (!fileTable.isDefined(fileName)) {
            throw new MyException("File " + fileName.getVal() + " is not open.");
        }

        BufferedReader br = fileTable.lookup(fileName);
        try {
            br.close();
        } catch (IOException e) {
            throw new MyException("Error closing file " + fileName.getVal() + ": " + e.getMessage());
        }

        fileTable.remove(fileName);

        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }
    @Override
    public MyIDictionary<String, model.type.Type> typecheck(MyIDictionary<String, model.type.Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new StringType())) {
            return typeEnv;
        } else
            throw new MyException("CloseRFile stmt: expression is not of StringType");
    }
}