package esinf.model;

import java.util.Objects;

public class Operation {
    private String id;
    private String name;

    public Operation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        Operation item = (Operation) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
