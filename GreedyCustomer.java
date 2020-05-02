package cs2030.simulator;

import java.util.ArrayList;


/**
 * A GreedyCustomer is a Customer that will choose 
 * a Server with the shortest waiting queue.
 */
class GreedyCustomer extends Customer {

    /**
     * Initialises a GreedyCustomer.
     * @param id An integer that uniquely identifies the GreedyCustomer.
     * @param initialArrivalTime The initial arrival time of the GreedyCustomer.
     */
    GreedyCustomer(int id, double initialArrivalTime) {
        super(id, initialArrivalTime);
    }

    /**
     * Chooses a server with the shortest queue and 
     * gives its index in the list of Servers.
     * @param as Summary of all Servers.
     * @return The integer index of the chosen Server in the list of Servers.
     * @throws NoSuchElementException if no Server is able to accept this 
     * customer into their waiting queue.
     */
    @Override
    int chooseServerForWaitIndex(AllServerSummary as) {
        return as.getShortestWaitingIndex();
    }

    /**
     * Formats the GreedyCustomer's ID together with a greedy tag as string.
     * @return The ID with a greedy tag behind as a string.
     */
    @Override
    public String toString() {
        return super.toString() + "(greedy)";
    }
}