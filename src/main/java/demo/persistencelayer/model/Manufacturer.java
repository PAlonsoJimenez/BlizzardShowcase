package demo.persistencelayer.model;

public class Manufacturer {
    private String id;
    private final String name;
    private final String nationality;

    public Manufacturer(String id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
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

    public String getNationality() {
        return nationality;
    }
}
