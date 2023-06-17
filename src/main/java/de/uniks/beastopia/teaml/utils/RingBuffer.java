package de.uniks.beastopia.teaml.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class RingBuffer<T> implements Iterable<T> {
    private final ArrayList<T> buffer = new ArrayList<>();
    private final int size;

    public RingBuffer(int size) {
        this.size = size;
    }

    public void push(T t) {
        if (buffer.size() >= size) {
            buffer.remove(0);
        }
        buffer.add(t);
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    public void clear() {
        buffer.clear();
    }

    public void pop() {
        buffer.remove(buffer.size() - 1);
    }

    public T peek() {
        return buffer.get(buffer.size() - 1);
    }

    @Override
    public Iterator<T> iterator() {
        return buffer.iterator();
    }
}
