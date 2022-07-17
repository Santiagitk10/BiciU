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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiMenu {

    Reader reader = new Reader();
    DataSeeds dataseeds = new DataSeeds();

    private List<User> users = dataseeds.seedUsers();
    private List<Bicycle> bicycles = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
/*   private List<Ticket> tickets = dataseeds.seedTickets();*/



    public void displayMainMenu(){

        int option = 0;

        do{
            option = 0;
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

            try{
                option = reader.scannerInt();
            } catch(Exception e) {
                System.out.println("Enter Only Numbers");
            }



            switch (option){
                case 1:
                    createUser();
                    break;
                case 2:
                    borrowBicycle();
                    break;
                case 3:
                    returnBicycle();
                    break;
                case 4:
                    payTicket();
                    break;
                case 5:
                    displayTickets();
                    break;
            }

        } while(option != 6);

    }


    public void displayTickets(){
        System.out.println(
                "1. Show All Tickets\n" +
                        "2. Search by Code\n" +
                        "3. Search by Status"
        );
        int option;
        try{
            option = reader.scannerInt();
        } catch(Exception e) {
            System.out.println("Enter Only Numbers");
            return;
        }
        if(option < 1 || option > 3 ){
            System.out.println("Select only of the the three options");
            return;
        }
        switch (option){
            case 1:
                readTicketsFromTxt();
                Collections.sort(tickets, new Comparator<Ticket>(){
                    public int compare(Ticket t1, Ticket t2){
                        return Integer.valueOf(t1.getTicketCode().compareTo(t2.getTicketCode()));
                    }

                });
                System.out.format("%6s%20s%20s%20s%20s%n", "Code", "UserID", "Name", "Amount ($)", "Status");
                for(Ticket ticket:tickets){
                    ticket.printTicketTable();
                }

                // TODO https://www.youtube.com/watch?v=wzWFQTLn8hI COMPARATOR IMPLEMENTAR Y SOLO ES
                //TODO LEER EL TEXT FILE, ORGANIZAR, FILTRAR CON UN LOOP Y CREAR UN MÉTODO PARA MOSTRAR EN LA CLASE TICKET.
                // TODO Y CON ESO TERMINO!
                break;
        }
    }


    public void payTicket(){
        System.out.println("Enter ticket Number to Pay");
        String ticktToPay = reader.scannerText();
        readTicketsFromTxt();
        for(Ticket ticket: tickets){
            if(ticket.getTicketCode().equals(ticktToPay)){
                if(ticket.getTicketStatus() == TicketStatus.OK) {
                    System.out.println("The Ticket has no debt");
                    ticket.displayTicketInfo();
                    return;
                } else if (ticket.getTicketStatus() == TicketStatus.ACTIVE) {
                    System.out.println("The Bicycle has not been returned");
                    return;
                } else {
                    ticket.displayTicketInfo();
                    System.out.println("Proceed to Pay? Y/N");
                    String pay = reader.scannerText().toUpperCase();
                    if(!pay.equals("Y") && !pay.equals("N")){
                        System.out.println("Enter only Y or N");
                        return;
                    } else if (pay.equals("N")){
                        return;
                    }

                    ticket.setTicketStatus(TicketStatus.OK);
                    ticket.setAmountToPay(0);
                    for(User user: users){
                        if(ticket.getUserId().equals(user.getDni())){
                            user.setDebt(0);
                            break;
                        }
                    }
                    System.out.println("Ticket Updated!");
                    ticket.displayTicketInfo();
                    writeTicketsToTxt();
                }
                return;
            }
        }
        System.out.println("The Ticket Does not Exist");

    }


    public void returnBicycle(){
        System.out.println("Enter ticket ID to return Bicycle");
        String ticketIdToReturn = reader.scannerText();
        for(Ticket ticket: tickets){
            if(ticket.getTicketCode().equals(ticketIdToReturn)){
                if(ticket.getTicketStatus() != TicketStatus.ACTIVE){
                    System.out.println("The bicycle was already returned");
                    return;
                }
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

                    if(user.getDni().equals(ticket.getUserId())){
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
                        if(user.getDebt() == 0){
                            ticket.setTicketStatus(TicketStatus.OK);
                        } else if(user.getDebt() > 0){
                            ticket.setTicketStatus(TicketStatus.PENDING);
                        }


                        ticket.displayTicketInfo();

                        for(Bicycle bike: bicycles){
                            if(bike.getIdCode().equals(ticket.getBicycleCode())){
                                bike.setAvailable(true);
                                break;
                            }
                        }

                        updateBicycleTxtFile();
                        writeTicketsToTxt();

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




    //TODO FALTA TESTEARLA CUANDO SE LEAN LOS TICKETS PARA PAGARLOS
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
                LocalTime endTime;
                try{
                    endTime = LocalTime.parse(data[6]);
                } catch (Exception e){
                    endTime = LocalTime.of(0,0,0);
                }
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

            br.close();

            /*boolean found = false;

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
                                ";" + ticket.getAmountToPay() + "\n");
                    }

                    bw2.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

    }





    public Bicycle selectBicycle(String chosenBicycleType){

        BicycleType requestedType = BicycleType.ROAD;
        if(chosenBicycleType.equals("M")){
            requestedType = BicycleType.MOUNTAIN;
        }

        Bicycle returnBike = null;


            readBicyclesTxtFile();

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


        return returnBike;
    }



    public void readBicyclesTxtFile(){
        bicycles.clear();
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


    public User isUserAlreadyRegistered(String dni){
        for(User user: users){
            if(user.getDni().equals("S-" + dni) || user.getDni().equals("P-" + dni)){
               return user;
            }
        }
        return null;
    }

}





