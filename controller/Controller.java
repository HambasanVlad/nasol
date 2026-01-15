package controller;

import exception.MyException;
import model.PrgState;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repo;
    private boolean displayFlag = true;
    private ExecutorService executor;

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    public void setDisplayFlag(boolean value) {
        this.displayFlag = value;
    }

    // --- GARBAGE COLLECTOR ---
    Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue)v; return v1.getAddr();})
                .collect(Collectors.toList());
    }

    List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue)v; return v1.getAddr();})
                .collect(Collectors.toList());
    }

    // --- CONCURRENCY METHODS ---

    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        // Logare înainte
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
                if (displayFlag) System.out.println(prg.toString());
            } catch (MyException e) {
                System.out.println("Error logging: " + e.getMessage());
            }
        });

        // Execuție Concurentă
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> { return p.oneStep(); }))
                .collect(Collectors.toList());

        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        // Ignorăm erorile "Stack empty" sau "NoSuchElement" (thread terminat)
                        if (e.getCause() instanceof MyException || e.getCause() instanceof java.util.NoSuchElementException) {
                            return null;
                        }
                        System.out.println("Error in thread: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);

        // Logare după (ca să vedem rezultatul imediat)
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
                if (displayFlag) System.out.println(prg.toString());
            } catch (MyException e) {
                System.out.println("Error logging: " + e.getMessage());
            }
        });

        repo.setPrgList(prgList);
    }

    public void allStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());

        while(prgList.size() > 0) {
            // Garbage Collector
            List<Integer> allSymTableAddr = prgList.stream()
                    .map(p -> p.getSymTable().getContent().values())
                    .flatMap(Collection::stream)
                    .map(v -> (Value)v)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue)v).getAddr())
                    .collect(Collectors.toList());

            PrgState firstPrg = prgList.get(0);
            firstPrg.getHeap().setContent(
                    safeGarbageCollector(
                            allSymTableAddr,
                            getAddrFromHeap(firstPrg.getHeap().getContent().values()),
                            firstPrg.getHeap().getContent()
                    )
            );

            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdownNow();
        repo.setPrgList(prgList);
    }
}