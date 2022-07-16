package ui;

import artifacts.Bicycle;
import artifacts.BicycleType;
import artifacts.Ticket;
import artifacts.TicketStatus;
import user.Rol;
import user.User;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiMenu {

    Reader reader = new Reader();
    DataSeeds dataseeds = new DataSeeds();

    private List<User> users = dataseeds.seedUsers();
    private List<Bicycle> bicycles = new ArrayList<>();
   /* private List<Ticket> tickets = new ArrayList<>();*/
   private List<Ticket> tickets = dataseeds.seedTickets();



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
                case 3:
                    returnBicycle();
            }

        } while(option != 6);


    }


    public void returnBicycle(){
        System.out.println("Enter ticket ID to return Bicycle");
        String ticketIdToReturn = reader.scannerText();
        for(Ticket ticket: tickets){
            if(ticket.getTicketCode().equals(ticketIdToReturn)){
                System.out.println("Did the user return the Helmet? Y/N");
                String helmetReturned = reader.scannerText().toUpperCase();
                if(!helmetReturned.equals("Y") && !helmetReturned.equals("N")){
                    System.out.println("Enter only Y or N");
                    return;
                }
                boolean isHelmetOk = true;
                if(helmetReturned.equals("N")){
                    isHelmetOk = false;
                }

                System.out.println("Is the Bicycle in good condition? Y/N");
                String goodCondition = reader.scannerText().toUpperCase();
                if(!goodCondition.equals("Y") && !goodCondition.equals("N")){
                    System.out.println("Enter only Y or N");
                    return;
                }
                boolean isBikeOk = true;
                if(goodCondition.equals("N")){
                    isBikeOk = false;
                }

                for(User user: users){
                    System.out.println(user.getDni()); //TODO DELETE
                    System.out.println(ticket.getUserId()); //TODO DELETE


                    if(user.getDni().equals(ticket.getUserId())){
                        System.out.println("Here"); //TODO DELETE
                        if(!isHelmetOk){
                            user.increaseDebt(5);
                        }
                        if(!isBikeOk){
                            user.increaseDebt(5);
                        }
                        user.decreaseDebt(1);

                        LocalTime finishTime = LocalTime.now();


                        long elapsedMinutes = Duration.between(ticket.getStartTime(), finishTime).toMinutes();

                        double timeCharge = Math.floor((elapsedMinutes / 30) * 3 - 3);
                        if(timeCharge < 0){
                            timeCharge = 0;
                        }

                        user.increaseDebt(timeCharge);

                        ticket.setAmountToPay(user.getDebt());
                        ticket.setHasHelmet(isHelmetOk);
                        ticket.setBicycleOk(isBikeOk);
                        ticket.setEndTime(finishTime);
                        ticket.setTicketStatus(TicketStatus.PENDING);

                        ticket.displayTicketInfo();

                        //TODO ACTUALIZAR BICI STATUS Y ESCRIBIR TICKETS
                        break;

                    }
                }


                return;
            }
        }

        System.out.println("The ticket ID does not exist. Enter a valid ticket ID");
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
        User returnedUser = isUserAlreadyRegistered(dni);
        if(returnedUser != null){
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
        User returnedUser = isUserAlreadyRegistered(userDni);
        if(returnedUser == null){
            System.out.println("The User is not registered. Go back and Create it");
            return;
        }


        if(returnedUser.getDebt() > 0){
            System.out.println("User " + userDni + " has a ticket with debt. " +
                    "Please cancel it and try again.");
            return;
        }

        System.out.println("Choose a Bicycle Type. Enter M for Mountain or R for Road");
        String chosenBicycleType = reader.scannerText().toUpperCase();
        if(chosenBicycleType.equals("M") || chosenBicycleType.equals("R")){
            Bicycle returnedBike = selectBicycle(chosenBicycleType);
            if(returnedBike != null){
                //When the user borrows a bike $1 is assigned to his/her debt to prevent the user
                //to lend a new bike while the current has not been returned. This is reverted upon return of the bike
                returnedUser.setDebt(1.0);
                Ticket ticket = new Ticket(returnedBike,returnedUser);
                ticket.displayTicketInfo();
                tickets.add(ticket);
                writeTicketsToTxt();

            }
        } else {
            System.out.println("Only Options M or R are valid");
            return;
        }




    }





    public void readTicketsFromTxt(){
        tickets.clear();
        /*BicycleType requestedType = BicycleType.ROAD;
        if(chosenBicycleType.equals("M")){
            requestedType = BicycleType.MOUNTAIN;
        }*/

        String filePath = "C:\\Users\\SANTIAGO SIERRA\\IdeaProjects\\BiciU\\src\\utilities\\tickets.txt";
        String currentLine;
        String data[];
/*        Bicycle returnBike = null;*/

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null){
                data = currentLine.split(";");

                LocalDate localDate = LocalDate.parse(data[4]);
                LocalTime startTime = LocalTime.parse(data[5]);
                LocalTime endTime = LocalTime.parse(data[6]);
                boolean isHelmetOk = true;
                if(data[7].equals("false")){
                    isHelmetOk = false;
                }
                boolean isBikeOk = true;
                if(data[8].equals("false")){
                    isBikeOk = false;
                }
                TicketStatus status = TicketStatus.ACTIVE;
                if(data[9].equals("PENDING")){
                    status = TicketStatus.PENDING;
                } else if(data[9].equals("OK")){
                    status = TicketStatus.OK;
                }
                double debt = Double.parseDouble(data[10]);

                Ticket ticket = new Ticket(data[0],data[1],data[2],data[3],localDate,startTime,endTime,isHelmetOk,isBikeOk,status,debt);
                tickets.add(ticket);

            }

            /*br.close();

            boolean found = false;

            for(Bicycle bike: bicycles){
                if(bike.getType().equals(requestedType) && bike.isAvailable()){
                    bike.displayBicycleSelection();
                    bike.setAvailable(false);
                    found = true;
                    returnBike = bike;
                    updateBicycleTxtFile();
                    break;
                }

            }

            if(!found){
                System.out.println("There are no " + requestedType + " bicycles available" +
                        " Try with other type or come back later");
                return null;

            }*/



        } catch (IOException e) {
            e.printStackTrace();
        }

      /*  return returnBike;*/
    }






    public void writeTicketsToTxt(){
        String filePath = "C:\\Users\\SANTIAGO SIERRA\\IdeaProjects\\BiciU\\src\\utilities\\tickets.txt";


                try {
                    FileWriter fw2 = new FileWriter(filePath);
                    BufferedWriter bw2 = new BufferedWriter(fw2);

                    for(Ticket ticket: tickets){

                        bw2.write(ticket.getTicketCode() + ";" + ticket.getBicycleCode() +
                                ";" + ticket.getUserId() + ";" + ticket.getUserName() + ";" +
                                ticket.getDate() + ";" + ticket.getStartTime() + ";" + ticket.getEndTime() +
                                ";" + ticket.isHasHelmet() + ";" + ticket.isBicycleOk() + ";" + ticket.getTicketStatus() +
                                ";" + ticket.getUser().getDebt() + "\n");
                    }

                    bw2.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }




    }





    public Bicycle selectBicycle(String chosenBicycleType){

        bicycles.clear();
        BicycleType requestedType = BicycleType.ROAD;
        if(chosenBicycleType.equals("M")){
            requestedType = BicycleType.MOUNTAIN;
        }

        String filePath = "C:\\Users\\SANTIAGO SIERRA\\IdeaProjects\\BiciU\\src\\utilities\\bicycleData.txt";
        String currentLine;
        String data[];
        Bicycle returnBike = null;

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

            br.close();

            boolean found = false;

            for(Bicycle bike: bicycles){
                if(bike.getType().equals(requestedType) && bike.isAvailable()){
                    bike.displayBicycleSelection();
                    bike.setAvailable(false);
                    found = true;
                    returnBike = bike;
                    updateBicycleTxtFile();
                    break;
                }

            }

            if(!found){
                System.out.println("There are no " + requestedType + " bicycles available" +
                        " Try with other type or come back later");
                return null;

            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnBike;
    }





    public void updateBicycleTxtFile(){
        String filePath = "C:\\Users\\SANTIAGO SIERRA\\IdeaProjects\\BiciU\\src\\utilities\\bicycleData.txt";

        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);

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


    //TODO DELETE IF I SEE NO NEED TO USE IT
    /*public boolean userHasDebt(String userDni){
        for(User user: users){
            System.out.println(user.getDebt() + " Debt en el método"); //TODO DELETE
            if(user.getDni().equals("S-" + userDni) || user.getDni().equals("P-" + userDni) && user.getDebt() > 0){
               return true;
            } else {
                return false;
            }
        }
        return false;
    }*/


    public User isUserAlreadyRegistered(String dni){
        for(User user: users){
            if(user.getDni().equals("S-" + dni) || user.getDni().equals("P-" + dni)){
               return user;
            }
        }
        return null;
    }

}





