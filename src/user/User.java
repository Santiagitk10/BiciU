package user;

public class User {

    private Rol rol;
    private String dni;
    private String completeName;
    private int age;
    private double debt;

    public User(Rol rol, String dni, String completeName, int age) {
        this.rol = rol;
        this.dni = dni;
        this.completeName = completeName;
        this.age = age;
        this.debt = 0;
    }
}
