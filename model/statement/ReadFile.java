package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.IntType;
import model.type.StringType;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt {
    private final Exp exp;
    private final String var_name;

    public ReadFile(Exp exp, String var_name) {
        this.exp = exp;
        this.var_name = var_name;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        if (!symTbl.isDefined(var_name)) {
            throw new MyException("Variable " + var_name + " is not defined.");
        }
        if (!symTbl.lookup(var_name).getType().equals(new IntType())) {
            throw new MyException("Variable " + var_name + " is not of type int.");
        }

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
            String line = br.readLine();
            int readValue;
            if (line == null || line.isEmpty()) {
                readValue = 0;
            } else {
                readValue = Integer.parseInt(line);
            }
            symTbl.update(var_name, new IntValue(readValue));
        } catch (IOException e) {
            throw new MyException("Error reading from file " + fileName.getVal() + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new MyException("File " + fileName.getVal() + " does not contain an integer.");
        }

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + ", " + var_name + ")";
    }
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        Type typevar = typeEnv.lookup(var_name);

        if (typexp.equals(new StringType())) {
            if (typevar.equals(new IntType())) {
                return typeEnv;
            } else {
                throw new MyException("ReadFile: variable " + var_name + " is not of type int");
            }
        } else {
            throw new MyException("ReadFile: expression " + exp.toString() + " is not of type string");
        }
    }
}