package artifacts;

import user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket {

    private static int ticketCodeCounter;
    private String ticketCode;
    private Bicycle assignedBicycle;
    private String bicycleCode;
    private User user;
    private String userId;
    private String userName;
    private double amountToPay;
    private TicketStatus ticketStatus;
    private boolean hasHelmet;
    private boolean isBicycleOk;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    public Ticket(Bicycle assignedBicycle, User user) {
        this.ticketCode = createTicketCode();
        this.assignedBicycle = assignedBicycle;
        this.bicycleCode = this.assignedBicycle.getIdCode();
        this.user = user;
        this.userId = this.user.getDni();
        this.userName = this.user.getCompleteName();
        this.amountToPay = this.user.getDebt();
        this.date = LocalDate.now();
        this.startTime = LocalTime.now();
        this.hasHelmet = true;
        this.isBicycleOk = true;
        this.ticketStatus = TicketStatus.ACTIVE;
    }

    public Ticket(String ticketCode, String bicycleCode, String userId,
                  String userName, LocalDate date, LocalTime startTime, LocalTime endTime,
                  boolean hasHelmet, boolean isBicycleOk,
                  TicketStatus ticketStatus, double amountToPay) {

        this.ticketCode = ticketCode;
        this.bicycleCode = bicycleCode;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasHelmet = hasHelmet;
        this.isBicycleOk = isBicycleOk;
        this.ticketStatus = ticketStatus;
        this.amountToPay = amountToPay;
    }


    public Bicycle getAssignedBicycle() {
        return assignedBicycle;
    }

    public User getUser() {
        return user;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public boolean isHasHelmet() {
        return hasHelmet;
    }

    public boolean isBicycleOk() {
        return isBicycleOk;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getBicycleCode() {
        return bicycleCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
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

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
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
                        "Bicycle: " + this.bicycleCode + "\n" +
                        "User: " + this.userId + "\n" +
                        "Name: " + this.userName + "\n" +
                        "Date: " + this.date + "\n" +
                        "Start time: " + this.startTime + "\n" +
                        "End time: " + this.endTime + "\n" +
                        "Has Helmet: " + this.hasHelmet + "\n" +
                        "Good condition: " + this.isBicycleOk + "\n" +
                        "Status: " + this.ticketStatus + "\n" +
                        "Amount: " + this.amountToPay
        );
    }

}


