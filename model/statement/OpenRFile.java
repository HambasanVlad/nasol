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
import java.io.FileReader;
import java.io.IOException;

public class OpenRFile implements IStmt {
    private final Exp exp;

    public OpenRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        // --- FIX AICI: Adăugat state.getHeap() ---
        Value val = exp.eval(symTbl, state.getHeap());

        if (!val.getType().equals(new StringType())) {
            throw new MyException("Expression did not evaluate to a string.");
        }

        StringValue fileName = (StringValue) val;
        if (fileTable.isDefined(fileName)) {
            throw new MyException("File " + fileName.getVal() + " is already open.");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName.getVal()));
            fileTable.put(fileName, br);
        } catch (IOException e) {
            throw new MyException("Could not open file " + fileName.getVal() + ": " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }
    @Override
    public MyIDictionary<String, model.type.Type> typecheck(MyIDictionary<String, model.type.Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new StringType())) {
            return typeEnv;
        } else
            throw new MyException("OpenRFile: expression is not of type string");
    }
}