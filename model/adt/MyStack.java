package model.adt;

import exception.MyException;
import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> implements MyIStack<T> {
    // java.util.Stack is old; ArrayDeque is the recommended implementation for stack operations.
    private Deque<T> stack;

    public MyStack() {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public T pop() throws MyException {
        if (stack.isEmpty()) {
            throw new MyException("Stack is empty. Cannot pop.");
        }
        return stack.pop();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public T peek() throws MyException {
        if (stack.isEmpty()) {
            throw new MyException("Stack is empty. Cannot peek.");
        }
        return stack.peek();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Iterate to print from top to bottom
        for (T elem : stack) {
            sb.append(elem.toString()).append("\n");
        }
        return sb.toString();
    }
}