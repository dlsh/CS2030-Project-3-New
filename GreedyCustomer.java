package cs2030.simulator;

import java.util.ArrayList;
import java.util.NoSuchElementException;



/**
 * A <code>GreedyCustomer</code> is a <code>Customer</code> that will choose 
 * a <code>Server</code> with the shortest waiting queue.
 */
class GreedyCustomer extends Customer {

    /**
     * Initialises a <code>GreedyCustomer</code>.
     * @param id An integer that uniquely identifies the <code>GreedyCustomer</code>
     * @param initialArrivalTime The initial arrival time of the <code>GreedyCustomer</code>
     */
    GreedyCustomer(int id, double initialArrivalTime) {
        super(id, initialArrivalTime);
    }

    /**
     * Chooses a <code>Server</code> with the shortest queue and 
     * gives its index in the list of <code>Server</code>s.
     * @param as Summary of all <code>Server</code>s
     * @return The integer index of the chosen <code>Server</code> in the list of 
     *     <code>Server</code>s
     * @throws NoSuchElementException if no <code>Server</code> is able to accept this 
     *     <code>Customer</code> into their waiting queue
     */
    @Override
    int chooseServerForWaitIndex(AllServerSummary as) {
        return as.getShortestWaitingIndex();
    }

    /**
     * Formats the <code>GreedyCustomer</code>'s ID together with a greedy tag as string.
     * @return The ID with a greedy tag behind as a string.
     */
    @Override
    public String toString() {
        return super.toString() + "(greedy)";
    }
}