package demo.weblayer.restModel;

public class Beer {
    private String id;
    private String name;
    private float graduation;
    private String type;
    private String description;
    private String manufacturerName;
    private String manufacturerId;

    private Beer() {
        // For Jackson
    }

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
