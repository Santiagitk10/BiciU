package artifacts;

import user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket {

    private static int ticketCodeCounter;
    private String ticketCode;
    private Bicycle assignedBicycle;
    private User user;
    private double amountToPay;
    private TicketStatus ticketStatus;
    private boolean hasHelmet;
    private boolean isBicycleOk;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    public Ticket(Bicycle assignedBicycle, User user) {
        this.assignedBicycle = assignedBicycle;
        this.user = user;
        this.date = LocalDate.now();
        this.startTime = LocalTime.now();
        this.ticketCode = createTicketCode();
        this.hasHelmet = true;
        this.isBicycleOk = true;
        this.ticketStatus = TicketStatus.ACTIVE;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public void setHasHelmet(boolean hasHelmet) {
        this.hasHelmet = hasHelmet;
    }

    public void setBicycleOk(boolean bicycleOk) {
        isBicycleOk = bicycleOk;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String createTicketCode(){
        int temporalTicketCounter = ticketCodeCounter;
        ticketCodeCounter++;
        return "T-" + String.format("%03d",temporalTicketCounter);
    }

    public void displayTicketInfo(){
        System.out.println(
                "A Ticket was generated\n" +
                        "Code: " + this.ticketCode + "\n" +
                        "Bicycle: " + this.assignedBicycle.getIdCode() + "\n" +
                        "User: " + this.user.getDni() + "\n" +
                        "Name: " + this.user.getCompleteName() + "\n" +
                        "Date: " + this.date + "\n" +
                        "Start time: " + this.startTime + "\n" +
                        "End time: " + this.endTime + "\n" +
                        "Has Helmet: " + this.hasHelmet + "\n" +
                        "Good condition: " + this.isBicycleOk + "\n" +
                        "Status: " + this.ticketStatus + "\n" +
                        "Amount: " + this.user.getDebt()
        );
    }

}


