package de.uniks.beastopia.teaml.utils;

public class Variant <T, U> {
    private T first = null;
    private U second = null;

    public Variant() {
    }

    public void setT(T t) {
        first = t;
        second = null;
    }

    public void setU(U u) {
        second = u;
        first = null;
    }

    public T getT() {
        return first;
    }

    public U getU() {
        return second;
    }

    public boolean isT() {
        return first != null;
    }

    public boolean isU() {
        return second != null;
    }
}
