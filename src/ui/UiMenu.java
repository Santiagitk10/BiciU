package ui;

import user.Rol;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiMenu {

    Reader reader = new Reader();

    private List<User> users = new ArrayList<>();



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


    public boolean isValidUserName(String completeName){
        String regex = "^[A-Z][a-z]+\\s[A-Z][a-z]+$";
        Pattern p = Pattern.compile(regex);
        if(completeName == null){
            return false;
        }
        Matcher m = p.matcher(completeName);
        return m.matches();
    }

}
