package user;

public class User {

    private Rol rol;
    private String dni;
    private String completeName;
    private int age;
    private double debt;

    public User(Rol rol, String dni, String completeName, int age) {
        this.rol = rol;
        this.dni = refactorDni(dni);
        this.completeName = completeName;
        this.age = age;
        this.debt = 0;
    }

    public String refactorDni(String dni){
       return this.rol == Rol.STUDENT ?  "S-" + dni :  "P-" + dni;
    }

    public String getDni() {
        return dni;
    }

    public String getCompleteName() {
        return completeName;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public void increaseDebt(double debtIncrement){
        this.debt += debtIncrement;
    }

    public void decreaseDebt(double debtDecrement){
        this.debt -= debtDecrement;
    }

    public  void printUserRegistration(){
        System.out.println(
                "¡Registration Completed \n\n" +
                "ID: " + this.dni + "\n" +
                "Name: " + this.completeName + "\n" +
                "Age: " + this.age
        );
    }

}
