package repository;

import exception.MyException;
import model.PrgState;
import java.util.List;

public interface IRepository {
    // 1. Metoda getCrtPrg a fost eliminata conform cerintelor A5

    void addPrgState(PrgState prg);

    // 2. Metode noi pentru gestionarea Listei de Thread-uri
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> list);

    void logPrgStateExec(PrgState prg) throws MyException;
}