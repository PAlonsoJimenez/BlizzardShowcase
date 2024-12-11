package demo.weblayer.restModel;

public class Manufacturer {
    private String id;
    private String name;
    private String nationality;

    private Manufacturer() {
        // For Jackson
    }

    public Manufacturer(String id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }
}
