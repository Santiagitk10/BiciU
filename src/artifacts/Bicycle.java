package artifacts;

public class Bicycle {

    private String idCode;
    private BicycleType type;
    private String color;
    private boolean isAvailable;

    public Bicycle(String idCode, BicycleType type, String color, boolean isAvailable) {
        this.idCode = idCode;
        this.type = type;
        this.color = color;
        this.isAvailable = isAvailable;
    }
}
