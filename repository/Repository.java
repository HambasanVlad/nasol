package repository;

import exception.MyException;
import model.PrgState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgStates; // Aceasta lista tine toate thread-urile active
    private final String logFilePath;

    public Repository(PrgState initialPrg, String logFilePath) {
        this.prgStates = new ArrayList<>();
        this.logFilePath = logFilePath;
        this.addPrgState(initialPrg); // Adaugam thread-ul principal

        // Golim fisierul de log la crearea repository-ului
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, false)))) {
            logFile.print("");
        } catch (IOException e) {
            System.out.println("Error clearing log file: " + e.getMessage());
        }
    }

    // --- Implementare Metode Noi A5 ---

    @Override
    public List<PrgState> getPrgList() {
        return prgStates;
    }

    @Override
    public void setPrgList(List<PrgState> list) {
        this.prgStates = list;
    }

    @Override
    public void addPrgState(PrgState prg) {
        prgStates.add(prg);
    }

    // --- Metoda getCrtPrg a fost stearsa ---

    @Override
    public void logPrgStateExec(PrgState prg) throws MyException {
        // Deschidem fisierul in mod 'append' (true)
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {

            // Scriem reprezentarea completa a starii (toString din PrgState a fost actualizat sa contina ID, Heap etc.)
            logFile.println(prg.toString());

            // Adaugam un separator vizual intre afisari
            logFile.println("----------------------------------------------------------------");
            logFile.println();

        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }
}