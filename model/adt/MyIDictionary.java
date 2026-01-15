package model.adt;

import exception.MyException;
import java.util.Map;

public interface MyIDictionary<K, V> {
    void put(K key, V value) throws MyException;
    void update(K key, V value) throws MyException;
    V lookup(K key) throws MyException;
    boolean isDefined(K key);
    void remove(K key) throws MyException; // ADAUGAT
    Map<K, V> getContent(); // IMPORTANT PENTRU LOGARE
    String toString();
    MyIDictionary<K, V> deepCopy(); // ADAUGAT
}