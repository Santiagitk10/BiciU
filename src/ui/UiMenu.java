package ui;

import user.Rol;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiMenu {

    Reader reader = new Reader();
    DataSeeds dataseeds = new DataSeeds();

    private List<User> users = dataseeds.seedUsers();



    public void displayMainMenu(){

        int option = 0;

        do{
            System.out.println("""
                
                BiciU - Management System
                
                Enter a numerical Option:
                
                1. Register User
                2. Borrow Bicycle
                3. Return Bicycle
                4. Pay ticket
                5. Tickets History
                6. Exit
                """);

                option = reader.scannerInt();

            switch (option){
                case 1:
                    createUser();
                    break;
                case 2:
                    borrowBicycle();
                    break;
            }

        } while(option != 6);


    }

    public void createUser(){

        System.out.println("What type of user do you wish to create. " +
                    "Enter S for Student P for Professor");
        String typeOfUser = reader.scannerText().toUpperCase();
        if(!typeOfUser.equals("S") && !typeOfUser.equals("P")){
            System.out.println("Only Options S or P are valid");
            return;
        }
        Rol rol = (typeOfUser.equals("S")) ? Rol.STUDENT : Rol.PROFESSOR;

        System.out.println("Enter the User´s DNI");
        String dni = reader.scannerText();
        if(isUserAlreadyRegistered(dni)){
            System.out.println("The User is already registered");
            return;
        }

        System.out.println("Enter the User´s complete name (Name Lastname) No accent maks");
        String completeName = reader.scannerText();
        if(!isValidUserName(completeName)) {
            System.out.println("Enter the name in the Requested Format");
            return;
        }

        System.out.println("Enter the User´s age");
        int age = reader.scannerInt();
        if(age < 18){
            System.out.println("The User must be an adult to lend a Bike");
            return;
        }

        User user = new User(rol,dni,completeName,age);
        user.printUserRegistration();
        users.add(user);

    }


    public void borrowBicycle(){
        System.out.println("En the User´s DNI");
        String userDni = reader.scannerText();
        if(!isUserAlreadyRegistered(userDni)){
            System.out.println("The User is not registered. Go back and Create it");
            return;
        }

        if(userHasDebt(userDni)){
            System.out.println("User " + userDni + " has a ticket with debt. " +
                    "Please cancel it and try again.");
            return;
        }



    }


    public boolean isValidUserName(String completeName){
        String regex = "^[A-Z][a-z]+\\s[A-Z][a-z]+$";
        Pattern p = Pattern.compile(regex);
        if(completeName == null){
            return false;
        }
        Matcher m = p.matcher(completeName);
        return m.matches();
    }



    public boolean userHasDebt(String userDni){
        for(User user: users){
            if(user.getDni().equals("S-" + userDni) || user.getDni().equals("P-" + userDni) && user.getDebt() > 0){
               return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public boolean isUserAlreadyRegistered(String dni){
        for(User user: users){
            if(user.getDni().equals("S-" + dni) || user.getDni().equals("P-" + dni)){
               return true;
            }
        }
        return false;
    }

}
