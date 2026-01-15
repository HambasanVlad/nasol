package model.adt;

import exception.MyException;
import java.util.Map;

// ACUM ACCEPTA DOI PARAMETRI: K (Key/Address) si V (Value/Content)
public interface MyIHeap<K, V> {
    int allocate(V value);
    V get(K key) throws MyException;
    void put(K key, V value);
    boolean containsKey(K key);
    void update(K key, V value);
    void setContent(Map<K, V> content);
    Map<K, V> getContent();
}