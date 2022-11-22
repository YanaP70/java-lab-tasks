package task02.wrapper;

import java.util.*;

public class SimpleModel {
    private int id;

    public SimpleModel() {
    }

    public SimpleModel(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleModel)) return false;
        SimpleModel that = (SimpleModel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SimpleModel{" +
                "id=" + id +
                '}';
    }
}
