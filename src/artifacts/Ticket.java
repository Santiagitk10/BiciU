package artifacts;

import user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket {

    private static int ticketCodeCounter;
    private String ticketCode;
    private User user;
    private double amountToPay;
    private TicketStatus ticketStatus;
    private boolean hasHelmet;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    public Ticket(User user, LocalDate date, LocalTime startTime) {
        this.user = user;
        this.date = date;
        this.startTime = startTime;
        this.ticketCode = createTicketCode();
    }


    public String createTicketCode(){
        int temporalTicketCounter = ticketCodeCounter;
        ticketCodeCounter++;
        return "T-" + String.format("%03d",temporalTicketCounter);
    }

}


