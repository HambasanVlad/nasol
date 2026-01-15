package model;

import exception.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.adt.MyIList;
import model.adt.MyIStack;
import model.statement.IStmt;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private MyIHeap<Integer, Value> heap;
    private IStmt originalProgram;

    // --- A5: ID Management ---
    private int id;
    private static int lastId = 0; // Static field for managing IDs [cite: 36]

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl,
                    MyIList<Value> ot, MyIDictionary<StringValue, BufferedReader> fileTable,
                    MyIHeap<Integer, Value> heap, IStmt prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = prg;

        // Asignare ID unic sincronizat
        this.id = getNewId();

        if (prg != null) {
            stk.push(prg);
        }
    }

    // Metodă statică sincronizată pentru generarea ID-urilor [cite: 36]
    public static synchronized int getNewId() {
        lastId++;
        return lastId;
    }

    public int getId() {
        return id;
    }

    // --- Getters & Setters ---
    public MyIStack<IStmt> getStk() { return exeStack; }
    public void setStk(MyIStack<IStmt> exeStack) { this.exeStack = exeStack; }

    public MyIDictionary<String, Value> getSymTable() { return symTable; }
    public void setSymTable(MyIDictionary<String, Value> symTable) { this.symTable = symTable; }

    public MyIList<Value> getOut() { return out; }
    public void setOut(MyIList<Value> out) { this.out = out; }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() { return fileTable; }
    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable) { this.fileTable = fileTable; }

    public MyIHeap<Integer, Value> getHeap() { return heap; }
    public void setHeap(MyIHeap<Integer, Value> heap) { this.heap = heap; }

    public IStmt getOriginalProgram() { return originalProgram; }

    // --- A5: Metoda isNotCompleted  ---
    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    // --- A5: Metoda oneStep (mutată din Controller) [cite: 20, 27-32] ---
    public PrgState oneStep() throws MyException {
        if (exeStack.isEmpty()) {
            throw new MyException("PrgState stack is empty");
        }
        IStmt crtStmt = exeStack.pop();
        // Returnează null dacă e o instrucțiune obișnuită, sau un nou PrgState dacă e Fork
        return crtStmt.execute(this);
    }

    // --- A5: toString Modificat  ---
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Afișăm ID-ul la început
        sb.append("ID: ").append(id).append("\n");

        sb.append("--- Execution Stack ---\n");
        sb.append(exeStack.toString());
        sb.append("\n--- Symbol Table ---\n");
        sb.append(symTable.toString());
        sb.append("\n--- Output ---\n");
        sb.append(out.toString());
        sb.append("\n--- File Table ---\n");
        sb.append(fileTable.toString());
        sb.append("\n--- Heap ---\n");
        sb.append(heap.toString());

        return sb.toString();
    }
}