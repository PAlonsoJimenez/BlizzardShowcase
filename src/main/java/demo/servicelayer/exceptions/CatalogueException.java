package demo.servicelayer.exceptions;

public class CatalogueException extends RuntimeException {
    private final CatalogueExceptionTypes catalogueExceptionType;

    public enum CatalogueExceptionTypes {
        BAD_AUTHORIZATION, INVALID_INPUT, BEER_NOT_FOUND, MANUFACTURER_NOT_FOUND
    }

    public CatalogueException(CatalogueExceptionTypes type) {
        super(generateMessage(type, ""));
        this.catalogueExceptionType = type;
    }

    public CatalogueException(CatalogueExceptionTypes type, String additionalMessage) {
        super(generateMessage(type, additionalMessage));
        this.catalogueExceptionType = type;
    }

    public CatalogueExceptionTypes getCatalogueExceptionType() {
        return catalogueExceptionType;
    }

    private static String generateMessage(CatalogueExceptionTypes type, String additionalMessage) {
        return getExceptionTypeMessage(type) + " " + additionalMessage;
    }

    private static String getExceptionTypeMessage(CatalogueExceptionTypes type) {
        return switch (type) {
            case BAD_AUTHORIZATION -> "Invalid Authorization Token";
            case INVALID_INPUT -> "Invalid Catalogue Input";
            case BEER_NOT_FOUND -> "Beer not found";
            case MANUFACTURER_NOT_FOUND -> "Manufacturer not found";
            default -> "Catalogue Exception.";
        };
    }
}
