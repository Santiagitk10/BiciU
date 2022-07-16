package ui;

import artifacts.Ticket;
import artifacts.TicketStatus;
import user.Rol;
import user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataSeeds {

    public List<User> seedUsers(){
        List<User> seedUsers = new ArrayList<>();

        User user1 = new User(Rol.STUDENT,"12345", "Santiago Sierra", 19);
        User user2 = new User(Rol.PROFESSOR,"12346", "Santiago Herrera", 29);
        User user3 = new User(Rol.STUDENT,"12347", "Santiago Nadal", 39);
        seedUsers.add(user1);
        seedUsers.add(user2);
        seedUsers.add(user3);

        return seedUsers;
    }

    public List<Ticket> seedTickets(){
        List<Ticket> seedTickets = new ArrayList<>();

        Ticket ticket1 = new Ticket("T-000","BIC-16","S-9876","Rafa Nada", LocalDate.of(2022,07,16), LocalTime.of(11,02,50),null,true,true, TicketStatus.ACTIVE,1.0);
        seedTickets.add(ticket1);

        return seedTickets;
    }

}
