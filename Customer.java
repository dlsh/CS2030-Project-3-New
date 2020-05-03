package cs2030.simulator;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.NoSuchElementException;




/**
 * Models a <code>Customer</code>. A <code>Customer</code> is initialised 
 * with ID and their initial arrival time.
 */
class Customer implements Comparable<Customer> {

    /**
     * Identify each <code>Customer</code> with an integer.
     */
    private final int id;

    /**
     * Initial arrival time of the <code>Customer</code>.
     */
    private final double initialArrivalTime;

    /**
     * Initialises a <code>Customer</code> with an integer for their ID and an initial arrival time.
     * @param id An integer acting as an identifier
     * @param initialArrivalTime The initial arrival time of the <code>Customer</code>
     */
    Customer(int id, double initialArrivalTime) {
        this.id = id;
        this.initialArrivalTime = initialArrivalTime;
    }

    /**
     * Compare <code>Customer</code>s based on their ID.
     * @param c The <code>Customer</code> that is being compared to
     * @return 1, -1, 0 if this <code>Customer</code> is greater than, lesser than 
     *     or equals to the compared <code>Customer</code> <code>c</code>
     */
    public int compareTo(Customer c) {
        if (this.id > c.id) {
            return 1;
        } else if (this.id < c.id) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Chooses a <code>Server</code> to serve the <code>Customer</code> and 
     * returns the <code>Server</code>'s index in a list of <code>Server</code>s.
     * @param as An <code>AllServerSummary</code> object describing 
     *     the overall summary of all <code>Server</code>s
     * @return The index of the chosen <code>Server</code> in a list of <code>Server</code>s
     * @throws NoSuchElementException if no <code>Server</code> is able to serve 
     *     this <code>Customer</code> immediately
     */
    int chooseServerIndex(AllServerSummary as) {
        return as.getFirstAvaliableIndex();
    }

    /**
     * Chooses a <code>Server</code> with a queue for a <code>Customer</code> to wait and 
     * returns the <code>Server</code>'s index in a list of <code>Server</code>s.
     * @param as An <code>AllServerSummary</code> object describing 
     *     the overall summary of all <code>Server</code>s
     * @return The index of the chosen <code>Server</code> in a list of <code>Server</code>s
     * @throws NoSuchElementException if no <code>Server</code> is able to accept this 
     *     <code>Customer</code> into their waiting queue
     */
    int chooseServerForWaitIndex(AllServerSummary as) {
        return as.getFirstWaitingIndex();
    }

    /**
     * Find the total waiting time taken between the <code>Customer</code>'s initial arrival 
     * and them being served.
     * @param currentTime Time of serving event
     * @return The difference between <code>currentTime</code> and initial arriving time
     */
    double waitingTime(double currentTime) {
        return currentTime - this.initialArrivalTime;
    }

    /**
     * Returns the <code>Customer</code>'s ID in string format.
     * @return <code>Customer</code>'s ID as string
     */
    @Override
    public String toString() {
        return "" + this.id;
    }
}