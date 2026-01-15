package model.adt;

import exception.MyException;
import model.value.Value;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; // IMPORT IMPORTANT
import java.util.concurrent.atomic.AtomicInteger; // IMPORT IMPORTANT

public class MyHeap implements MyIHeap<Integer, Value> {
    // Folosim ConcurrentHashMap pentru ca thread-urile să nu corupă memoria
    private ConcurrentHashMap<Integer, Value> map;
    // AtomicInteger asigură că adresele (1, 2, 3...) nu se duplică între thread-uri
    private AtomicInteger freeLocation;

    public MyHeap() {
        this.map = new ConcurrentHashMap<>();
        this.freeLocation = new AtomicInteger(1);
    }

    @Override
    public int allocate(Value value) {
        int newLocation = freeLocation.getAndIncrement();
        map.put(newLocation, value);
        return newLocation;
    }

    @Override
    public Value get(Integer address) throws MyException {
        if (!map.containsKey(address)) {
            throw new MyException("Heap address " + address + " is invalid.");
        }
        return map.get(address);
    }

    @Override
    public void put(Integer address, Value value) {
        map.put(address, value);
    }

    @Override
    public boolean containsKey(Integer address) {
        return map.containsKey(address);
    }

    @Override
    public void update(Integer address, Value value) {
        map.put(address, value);
    }

    @Override
    public void setContent(Map<Integer, Value> content) {
        this.map.clear();
        this.map.putAll(content);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Value> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}