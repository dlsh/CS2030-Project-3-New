package cs2030.simulator;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.NoSuchElementException;




/**
 * Models a Customer. A Customer is initialised with ID and their initial arrival time.
 */
class Customer implements Comparable<Customer> {

    /**
     * Identify each Customer with an integer.
     */
    private final int id;

    /**
     * Initial arrival time of the Customer.
     */
    private final double initialArrivalTime;

    /**
     * Initialises a customer with an integer for their ID and an initial arrival time.
     * @param id An integer acting as an identifier.
     * @param initialArrivalTime The initial arrival time of the customer
     */
    Customer(int id, double initialArrivalTime) {
        this.id = id;
        this.initialArrivalTime = initialArrivalTime;
    }

    /**
     * Compare customers based on their ID.
     * @param c The customer that is being compared to.
     * @return 1, -1, 0 if this customer is greater than, lesser than 
     *     or equals to the compared customer <code>c</code>.
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
     * Chooses a server to serve the customer and returns the Server's index in a list of Servers.
     * @param as The overall summary of all Servers.
     * @return The index of the chosen Server in a list of Servers.
     * @throws NoSuchElementException if no Server is able to serve this customer immediately.
     */
    int chooseServerIndex(AllServerSummary as) {
        return as.getFirstAvaliableIndex();
    }

    /**
     * Chooses a server with a queue for a customer to wait and 
     * returns the Server's index in a list of Servers.
     * @param as The overall summary of all Servers.
     * @return The index of the chosen Server in a list of Servers.
     * @throws NoSuchElementException if no Server is able to accept this 
     * customer into their waiting queue.
     */
    int chooseServerForWaitIndex(AllServerSummary as) {
        return as.getFirstWaitingIndex();
    }

    /**
     * Find the total waiting time taken between the customer's initial arrival 
     * and them being served.
     * @param currentTime Time of serving event.
     * @return The difference between <code>currentTime</code> and initial arriving time
     */
    double waitingTime(double currentTime) {
        return currentTime - this.initialArrivalTime;
    }

    /**
     * Returns the Customer's ID in string format.
     * @return Customer ID as string.
     */
    @Override
    public String toString() {
        return "" + this.id;
    }
}