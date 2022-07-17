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

    public String getIdCode() {
        return idCode;
    }

    public String getColor() {
        return color;
    }

    public BicycleType getType() {
        return type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void displayBicycleSelection(){
        System.out.println(
                "Bicycle Chosen!\n" +
                        "Code: " + this.idCode + "\n" +
                        "Type: " + this.type + "\n" +
                        "Color: " + this.color + "\n"
        );
    }
}
