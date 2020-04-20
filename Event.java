package cs2030.simulator;
import java.lang.Comparable;
import java.util.Optional;

/**
 * Models an event.
 */
class Event implements Comparable<Event> {

    /**
     * Status types
     */
    enum Status {
        ARRIVES("arrives"),
        SERVED("served by"),
        LEAVES("leaves"),
        DONE("done serving by"),
        WAITS("waits to be served by"),
        SERVER_REST("done; resting by");

        private String status;

        private Status(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return this.status;
        }
    }

    private final Customer customerDetails;

    private final Optional<Server> serverDetails;

    private final double eventTime;

    private final Status eventStatus;

    Event(Customer customer, Optional<Server> server, double eventTime, Status eventStatus) {
        this.customerDetails = customer;
        this.serverDetails = server;
        this.eventTime = eventTime;
        this.eventStatus = eventStatus;
    }

    /**
     * Compares 2 Events by comparing their customer details.
     */
    public int compareTo(Event e) {
        // Earlier event time is first in the queue.
        if (this.eventTime > e.eventTime) {
            return 1;
        } else if (this.eventTime < e.eventTime) {
            return -1;
        } else {
            // For events occuring at the same time, compare based on customer
            if (this.customerDetails.compareTo(e.customerDetails) != 0) {
                return this.customerDetails.compareTo(e.customerDetails);
            
            // If customers are the same, compare based on event status
            } else {
                return this.eventStatus.compareTo(e.eventStatus);
            }
        }
    }

    /**
     * Static constructor method for arrival event.
     * @param arrivalTime Customer arrival time.
     * @param customerID Customer ID.
     * @param isGreedy Indicates whether customer is greedy/normal.
     * @return A new arriving event.
     */
    public static Event createArrivingEvent(double arrivalTime, int customerID, boolean isGreedy) {

        Customer c;
        
        if (isGreedy) {
            c = new GreedyCustomer(customerID, arrivalTime);
        } else { 
            c = new Customer(customerID, arrivalTime);
        }

        return new Event(c, Optional.empty(), arrivalTime, Status.ARRIVES);
    }


    Event createServingEvent(Customer c, Server s) {
        return new Event(c, Optional.ofNullable(s), this.eventTime, Status.SERVED);
    }

    Event createWaitingEvent(Customer c, Server s) {
        return new Event(c, Optional.ofNullable(s), this.eventTime, Status.WAITS);
    }

    Event createLeavingEvent() {
        return new Event(this.customerDetails, this.serverDetails, this.eventTime, Status.LEAVES);
    }

    Event createDoneEvent(double serviceTime) {
        return new Event(this.customerDetails, this.serverDetails, this.eventTime + serviceTime, Status.DONE);
    }

    Event createServerRestEvent(double restTime) {
        return new Event(this.customerDetails, this.serverDetails, this.eventTime + restTime, Status.SERVER_REST);
    }






    Customer getCustomerDetails() {
        return this.customerDetails;
    }

    Server getServerDetails() {
        return this.serverDetails.orElseThrow();
    }

    double getCustomerWaitingTime() {
        return this.customerDetails.waitingTime(this.eventTime);
    }


    boolean isArrival() {
        return this.eventStatus.equals(Status.ARRIVES);
    }

    boolean isServe() {
        return this.eventStatus.equals(Status.SERVED);
    }

    boolean isDone() {
        return this.eventStatus.equals(Status.DONE);
    }

    boolean isServerRest() {
        return this.eventStatus.equals(Status.SERVER_REST);
    }

    @Override
    public String toString() {
        String serverString;
        if (this.serverDetails.isPresent()) {
            serverString = " " + this.serverDetails.get().toString();
        } else {
            serverString = "";
        }
        return String.format("%.3f", this.eventTime) + " " + this.customerDetails + " " + this.eventStatus + serverString;
    }

}