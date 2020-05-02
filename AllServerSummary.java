package cs2030.simulator;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Summarises information on all Servers involved in the simulation. Allows for information on 
 * the overall Server situation to be passed to a Customer without cyclic dependency.
 */
class AllServerSummary {

    /**
     * Integer value representing a Server that is able to serve.
     */
    public static final int AVALIABLE = -1;
    /**
     * Integer value representing a Server that has a full waiting queue.
     */
    public static final int FULL = Integer.MAX_VALUE;

    /**
     * List of all Server statuses.
     */
    private final ArrayList<Integer> listOfServerStatus;

    /**
     * Constructs an empty AllServerSummary object with no Server status.
     */
    AllServerSummary() {
        this.listOfServerStatus = new ArrayList<Integer>();
    }

    /**
     * Adds the status of 1 Server into the AllServerSummary.
     * @param status Status of an individual Server.
     */
    void addStatus(int status) {
        listOfServerStatus.add(status);
    }

    /**
     * Finds the index of the first Server that can serve a new customer.
     * @return Index of the first Server that can serve a new customer as an integer.
     * @throws NoSuchElementException if all of the Servers are unable to serve a new customer.
     */
    int getFirstAvaliableIndex() {
        int result = this.listOfServerStatus.indexOf(AllServerSummary.AVALIABLE);

        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

    /**
     * Finds the index of the first Server that has a waiting queue that can accept additional customers.
     * @return Index of the first Server that has an avaliable waiting queue as an integer.
     * @throws NoSuchElementException if all of the Servers have full waiting queues.
     */
    int getFirstWaitingIndex() {
        int result = this.listOfServerStatus.indexOf(this.listOfServerStatus.stream().filter(x -> x != AllServerSummary.FULL).findFirst().get());

        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

    /**
     * Finds the index of the Server with the shortest waiting queue. 
     * In the event of a tie-break, the index of the first-occuring Server is returned.
     * @return Index of the Server with the shortest waiting queue as an integer.
     * @throws NoSuchElementException if all of the Servers have full waiting queues.
     */
    int getShortestWaitingIndex() {
        int result = this.listOfServerStatus.indexOf(this.listOfServerStatus.stream().filter(x -> x != AllServerSummary.FULL).min((x, y) -> x - y).get());
        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

}