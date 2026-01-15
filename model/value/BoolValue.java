package model.value;

import model.type.BoolType;
import model.type.Type;

public class BoolValue implements Value {
    private final boolean val;

    public BoolValue(boolean v) {
        this.val = v;
    }

    public boolean getVal() {
        return val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        BoolValue boolValue = (BoolValue) another;
        return val == boolValue.val;
    }
}