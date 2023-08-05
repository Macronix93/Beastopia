package de.uniks.beastopia.teaml.utils;

public class Variant<T, U, V> {
    private T first = null;
    private U second = null;
    private V third = null;

    public Variant() {
    }

    public void setT(T t) {
        first = t;
        second = null;
        third = null;
    }

    public void setU(U u) {
        second = u;
        first = null;
        third = null;
    }

    public void setV(V v) {
        third = v;
        first = null;
        second = null;
    }

    public T getT() {
        return first;
    }

    public U getU() {
        return second;
    }

    public V getV() {
        return third;
    }

    public boolean isT() {
        return first != null;
    }

    public boolean isU() {
        return second != null;
    }

    public boolean isV() {
        return third != null;
    }
}
