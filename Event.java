package cs2030.simulator;

import java.lang.Comparable;
import java.util.Optional;
import java.util.NoSuchElementException;

/**
 * An <code>Event</code> represents several types of occurences that occur in the shop 
 * environment that is being simulated in <code>EventSimulator</code>. The type of <code>
 * Event</code> is represented in terms of its <code>Status</code>, which is an enum. As 
 * such, an <code>Event</code> can be of the following types:
 * <p>An arrival <code>Event</code></p>
 * <p>A serving <code>Event</code></p>
 * <p>A leaving <code>Event</code></p>
 * <p>A done <code>Event</code></p>
 * <p>A waiting <code>Event</code></p>
 * <p>A <code>Server</code> resting <code>Event</code></p>
 */
class Event implements Comparable<Event> {

    /**
     * The <code>Event</code> <code>Status</code> enum represents the type of <code>Event</code>.
     * 6 different types are currently supported:
     * <p>An arrival <code>Event</code></p>
     * <p>A serving <code>Event</code></p>
     * <p>A leaving <code>Event</code></p>
     * <p>A done <code>Event</code></p>
     * <p>A waiting <code>Event</code></p>
     * <p>A <code>Server</code> resting <code>Event</code></p>
     */
    enum Status {
        /**
         * An arrival <code>Event</code>.
         */
        ARRIVES("arrives"),
        /**
         * A serving <code>Event</code>.
         */
        SERVED("served by"),
        /**
         * A leaving <code>Event</code>.
         */
        LEAVES("leaves"),
        /**
         * A done <code>Event</code>.
         */
        DONE("done serving by"),
        /**
         * A waiting <code>Event</code>.
         */
        WAITS("waits to be served by"),
        /**
         * A <code>Server</code> resting <code>Event</code>.
         */
        SERVER_REST("done; resting by");

        /**
         * This stores the description of the 
         * <code>Status</code> as a string.
         */
        private String status;

        /**
         * Constructs the <code>Status</code> enum.
         * @param status The description of the <code>Status</code>
         *     as a string
         */
        private Status(String status) {
            this.status = status;
        }

        /**
         * Gives the description of the <code>Status</code> as a string.
         * @return The description of the <code>Status</code>
         */
        @Override
        public String toString() {
            return this.status;
        }
    }

    /**
     * The <code>Customer</code> that is involved in the <code>Event</code>.
     * Note that for <code>Server</code> resting <code>Event</code>s, the 
     * previous <code>Customer</code> is not removed despite being done. 
     * They are only replaced when the <code>Server</code> finishes resting 
     * and serves a new <code>Customer</code>.
     */
    private final Customer customerDetails;

    /**
     * An <code>Optional</code> of a <code>Server</code>. This accounts for 
     * arrival <code>Event</code>s, which have no <code>Server</code> involved 
     * and is thus represented by an empty <code>Optional</code>.
     */
    private final Optional<Server> serverDetails;

    /**
     * The time at which the <code>Event</code> occurs.
     */
    private final double eventTime;

    /**
     * The type of <code>Event</code> that occurs.
     */
    private final Status eventStatus;

    /**
     * Constructs a new <code>Event</code> Object.
     */
    private Event(Customer customer, Optional<Server> server, 
            double eventTime, Status eventStatus) {
        this.customerDetails = customer;
        this.serverDetails = server;
        this.eventTime = eventTime;
        this.eventStatus = eventStatus;
    }

    /**
     * Compares 2 <code>Event</code>s by comparing several factors.
     * Firstly, the time at which the <code>Event</code>s occur.
     * If they occur at the same time, comparison of the 
     * <code>Customer</code> involved in each <code>Event</code>.
     * If both of the above factors are the same for both <code>Event</code>, 
     * comparison of the <code>Status</code> of both <code>Events</code> is done.
     * @param e The <code>Event</code> that this <code>Event</code> Object is 
     *     being compared to
     * @return 1 if this <code>Event</code> occurs earlier than <code>e</code>, -1 if it 
     *     occurs after <code>e</code>. If they both occur at the same time, 1 or -1 
     *     depending on the comparison of their <code>Customer</code>s. If their 
     *     <code>Customer</code> are the same as well, 1, -1 or 0 will be returned based 
     *     on the comparison of their <code>Status</code>.
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
     * Static constructor method for an arrival <code>Event</code>.
     * @param arrivalTime The <code>Customer</code> arrival time
     * @param customerID The <code>Customer</code> ID
     * @param isGreedy A boolean value indicating whether the involved 
     *     <code>Customer</code> is greedy/normal
     * @return A new arrival <code>Event</code>
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

    /**
     * Constructs a new serving <code>Event</code> based off an existing <code>Event</code> 
     * Object.
     * @param s The <code>Server</code> that will be serving the <code>Customer</code> described in
     *     this <code>Event</code>
     * @return A new serving <code>Event</code> with the participating <code>Server</code> described
     */
    Event createServingEvent(Server s) {
        return new Event(this.customerDetails, Optional.ofNullable(s), 
                this.eventTime, Status.SERVED);
    }

    /**
     * Constructs a new serving <code>Event</code> based off an existing <code>Event</code> 
     * Object. This overloaded function takes in an additional  <code>Customer</code>. 
     * This is for cases where the serving <code>Event</code> will serve a new 
     * <code>Customer</code> that is not described in the original <code>Event</code>.
     * @param c The new <code>Customer</code> that is served in the serving <code>Event</code>
     * @param s The <code>Server</code> that will be serving the <code>Customer c</code>.
     * @return A new serving <code>Event</code> with for <code>Customer c</code> and the 
     *     participating <code>Server</code>
     */
    Event createServingEvent(Customer c, Server s) {
        return new Event(c, Optional.ofNullable(s), this.eventTime, Status.SERVED);
    }

    /**
     * Constructs a new waiting <code>Event</code> based off an existing <code>Event</code> 
     * Object.
     * @param s The <code>Server</code> with a waiting queue that the <code>Customer</code> in the 
     *     existing <code>Event</code> joins
     * @return A new waiting <code>Event</code> updated with the particiapting <code>Server</code>
     */
    Event createWaitingEvent(Server s) {
        return new Event(this.customerDetails, Optional.ofNullable(s), 
                this.eventTime, Status.WAITS);
    }

    /**
     * Constructs a new leaving <code>Event</code> based off an existing <code>Event</code> 
     * Object.
     * @return A new leaving <code>Event</code>.
     */
    Event createLeavingEvent() {
        return new Event(this.customerDetails, this.serverDetails, this.eventTime, Status.LEAVES);
    }

    /**
     * Constructs a new done <code>Event</code> based off an existing <code>Event</code> 
     * Object. The time of this done <code>Event</code> will be calculated by adding 
     * <code>serviceTime</code> to the existing <code>eventTime</code>.
     * @param serviceTime The amount of time it takes for the <code>Customer</code> to be 
     *     serviced by the <code>Server</code>
     * @return A new done <code>Event</code> with an updated <code>eventTime</code>
     */
    Event createDoneEvent(double serviceTime) {
        return new Event(this.customerDetails, this.serverDetails, 
                this.eventTime + serviceTime, Status.DONE);
    }

    /**
     * Constructs a new <code>Server</code> resting <code>Event</code> based off an existing 
     * <code>Event</code> Object. The time of this resting <code>Event</code> is updated to 
     * the time at which the <code>Server</code> finishes resting. This ensures that the 
     * <code>Event</code> is only polled then. The time at which the <code>Server</code> 
     * finishes resting can be calculated by adding <code>restTime</code> to the existing 
     * <code>eventTime</code>.
     * @param restTime The amount of time that the <code>Server</code> rests for
     * @return A <code>Server</code> resting <code>Event</code> with the 
     *     <code>eventTime</code> updated till where the <code>Server</code> finishes resting
     */
    Event createServerRestEvent(double restTime) {
        return new Event(this.customerDetails, this.serverDetails, 
                this.eventTime + restTime, Status.SERVER_REST);
    }


    /**
     * Describes the <code>Customer</code> currently involved in the <code>Event</code>. 
     * Note that for <code>Server</code> resting <code>Event</code>s, it will describe 
     * the <code>Customer</code> that was just done being served instead since the 
     * <code>Customer</code> is not yet cleared.
     * @return The <code>Customer</code> involved in the <code>Event</code>, 
     *     with the exception of the <code>Server</code> resting <code>Event</code>
     */
    Customer getCustomerDetails() {
        return this.customerDetails;
    }

    /**
     * Describes the <code>Server</code> involved in this <code>Event</code>.
     * @return The <code>Server</code> involved in this <code>Event</code>
     * @throws NoSuchElementException if there is no <code>Server</code> 
     *     involved in this <code>Event</code>
     */
    Server getServerDetails() {
        return this.serverDetails.orElseThrow();
    }

    /**
     * Finds the amount of time the <code>Customer</code> involved in this <code>Event</code> 
     * has waited for since their original arrival time.
     * @return The differnce betwen the current <code>eventTime</code> and their 
     *     original arrival time
     */
    double getCustomerWaitingTime() {
        return this.customerDetails.waitingTime(this.eventTime);
    }

    /**
     * Checks whether if the <code>Event</code> is an arriving <code>Event</code>.
     * @return <code>true</code> if the <code>Event</code> is an arriving 
     *     <code>Event</code>, otherwise <code>false</code>
     */
    boolean isArrival() {
        return this.eventStatus.equals(Status.ARRIVES);
    }

    /**
     * Checks whether if the <code>Event</code> is a serving <code>Event</code>.
     * @return <code>true</code> if the <code>Event</code> is a serving 
     *     <code>Event</code>, otherwise <code>false</code>
     */
    boolean isServe() {
        return this.eventStatus.equals(Status.SERVED);
    }

    /**
     * Checks whether if the <code>Event</code> is a done <code>Event</code>.
     * @return <code>true</code> if the <code>Event</code> is a done 
     *     <code>Event</code>, otherwise <code>false</code>
     */
    boolean isDone() {
        return this.eventStatus.equals(Status.DONE);
    }

    /**
     * Checks whether if the <code>Event</code> is a <code>Server</code> 
     * resting <code>Event</code>.
     * @return <code>true</code> if the <code>Event</code> is a <code>Server</code> 
     *     resting <code>Event</code>, otherwise <code>false</code>
     */
    boolean isServerRest() {
        return this.eventStatus.equals(Status.SERVER_REST);
    }

    /**
     * Describes key details of the <code>Event</code> in string format.
     * @return The <code>eventTime</code>, the involved <code>Customer</code>, 
     *     the <code>eventStatus</code> and the involved <code>Server</code> if 
     *     present
     */
    @Override
    public String toString() {
        String serverString;
        if (this.serverDetails.isPresent()) {
            serverString = " " + this.serverDetails.get().toString();
        } else {
            serverString = "";
        }
        return String.format("%.3f", this.eventTime) + " " + 
                this.customerDetails + " " + this.eventStatus + serverString;
    }

}