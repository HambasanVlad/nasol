package model.adt;

import exception.MyException;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private Map<K, V> map;

    public MyDictionary() {
        this.map = new HashMap<>();
    }

    @Override
    public void put(K key, V value) throws MyException {
        map.put(key, value);
    }

    @Override
    public void update(K key, V value) throws MyException {
        if (!map.containsKey(key)) {
            throw new MyException("Variable " + key + " is not defined. Cannot update.");
        }
        map.put(key, value);
    }

    @Override
    public V lookup(K key) throws MyException {
        V value = map.get(key);
        if (value == null) {
            throw new MyException("Variable " + key + " is not defined.");
        }
        return value;
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public void remove(K key) throws MyException {
        if (!map.containsKey(key)) {
            throw new MyException("Variable " + key + " is not defined. Cannot remove.");
        }
        map.remove(key);
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    // --- DEEP COPY CORECT ---
    @Override
    public MyIDictionary<K, V> deepCopy() {
        MyIDictionary<K, V> toReturn = new MyDictionary<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            try {
                toReturn.put(entry.getKey(), entry.getValue());
            } catch (MyException e) {
                // Nu ar trebui să se întâmple
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey().toString())
                    .append(" -> ")
                    .append(entry.getValue().toString())
                    .append("\n");
        }
        return sb.toString();
    }
}