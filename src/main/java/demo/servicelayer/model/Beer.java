package demo.servicelayer.model;

public class Beer {
    private final String id;
    private final String name;
    private final float graduation;
    private final String type;
    private final String description;
    private final String manufacturerName;
    private final String manufacturerId;

    public Beer(String id, String name, float graduation, String type, String description, String manufacturerName, String manufacturerId) {
        this.id = id;
        this.name = name;
        this.graduation = graduation;
        this.type = type;
        this.description = description;
        this.manufacturerName = manufacturerName;
        this.manufacturerId = manufacturerId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getGraduation() {
        return graduation;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

}
