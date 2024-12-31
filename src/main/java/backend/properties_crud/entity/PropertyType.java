package backend.properties_crud.entity;

public enum PropertyType {
    ONE_RK("1RK"),
    TWO_RK("2RK"),
    ONE_BHK("1BHK"),
    TWO_BHK("2BHK"),
    THREE_BHK("3BHK"),
    BUNGALOW("BUNGALOW");
    // add more types as needed

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}