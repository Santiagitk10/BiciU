package ui;

import artifacts.Bicycle;
import artifacts.BicycleType;
import user.Rol;
import user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiMenu {

    Reader reader = new Reader();
    DataSeeds dataseeds = new DataSeeds();

    private List<User> users = dataseeds.seedUsers();
    private List<Bicycle> bicycles = new ArrayList<>();



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

        System.out.println("Choose a Bicycle Type. Enter M for Mountain or R for Road");
        String chosenBicycleType = reader.scannerText().toUpperCase();
        if(chosenBicycleType.equals("M") || chosenBicycleType.equals("R")){
            selectBicycle(chosenBicycleType);
        } else {
            System.out.println("Only Options M or R are valid");
            return;
        }




    }


    public void selectBicycle(String chosenBicycleType){

        bicycles.clear();
        BicycleType requestedType = BicycleType.ROAD;
        if(chosenBicycleType.equals("M")){
            requestedType = BicycleType.MOUNTAIN;
        }

        String filePath = "C:\\Users\\SANTIAGO SIERRA\\IdeaProjects\\BiciU\\src\\utilities\\bicycleData.txt";
        String currentLine;
        String data[];

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null){
                data = currentLine.split(";");

                    BicycleType bicycleType = BicycleType.MOUNTAIN;
                    if(data[1].equals("Road")){
                        bicycleType = BicycleType.ROAD;
                    }
                    boolean available = true;

                    if(data[3].equals("false")){
                        available = false;
                    }

                    Bicycle bicycle = new Bicycle(data[0],bicycleType,data[2], available);
                    bicycles.add(bicycle);

            }

            boolean found = false;
            for(Bicycle bike: bicycles){
                if(bike.getType().equals(requestedType) && bike.isAvailable()){
                    bike.displayBicycleSelection();
                    bike.setAvailable(false);
                    found = true;
                    updateBicycleTxtFile();
                    break;
                }

            }

            if(!found){
                System.out.println("There are no " + requestedType + " bicycles available" +
                        " Try with other type or come back later");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void updateBicycleTxtFile(){

        String filePath = "C:\\Users\\SANTIAGO SIERRA\\IdeaProjects\\BiciU\\src\\utilities\\bicycleData.txt";

        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
/*            PrintWriter pw = new PrintWriter(bw);*/

            for(Bicycle bike: bicycles){
                String type = "Road";
                String available = "false";
                if(bike.getType().equals(BicycleType.MOUNTAIN)){
                    type = "Mountain";
                }
                if(bike.isAvailable()){
                    available = "true";
                }
                bw.write(bike.getIdCode() + ";" + type + ";" + bike.getColor() + ";" + available + "\n");
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
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





