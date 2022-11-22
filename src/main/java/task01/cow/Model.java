package task01.cow;

import java.util.Objects;

public class Model {
    private int id;
    private String description;

    public Model() {
    }

    public Model(int id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Model)) return false;
        Model model = (Model) o;
        return id == model.id && Objects.equals(description, model.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}

