package cs2030.simulator;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Summarises information on all <code>Server</code>s involved in the simulation. 
 * Allows for information on the overall <code>Server</code> situation to be passed to a 
 * <code>Customer</code> without cyclic dependency.
 */
class AllServerSummary {

    /**
     * Integer value representing a <code>Server</code> that is able to serve.
     */
    public static final int AVALIABLE = -1;
    /**
     * Integer value representing a <code>Server</code> that has a full waiting queue.
     */
    public static final int FULL = Integer.MAX_VALUE;

    /**
     * List of all <code>Server</code> statuses.
     */
    private final ArrayList<Integer> listOfServerStatus;

    /**
     * Constructs an empty <code>AllServerSummary</code> object with no 
     * <code>Server</code> status currently stored.
     */
    AllServerSummary() {
        this.listOfServerStatus = new ArrayList<Integer>();
    }

    /**
     * Adds the status of 1 <code>Server</code> into the <code>AllServerSummary</code>.
     * @param status Status of an individual <code>Server</code>
     */
    void addStatus(int status) {
        listOfServerStatus.add(status);
    }

    /**
     * Finds the index of the first <code>Server</code> that can serve a new <code>Customer</code>.
     * @return Index of the first <code>Server</code> that can serve a new 
     *     <code>Customer</code> as an integer
     * @throws NoSuchElementException if all of the <code>Server</code>s are unable to serve 
     *     a new <code>Customer</code>
     */
    int getFirstAvaliableIndex() {
        int result = this.listOfServerStatus.indexOf(AllServerSummary.AVALIABLE);

        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

    /**
     * Finds the index of the first <code>Server</code> that has a waiting queue that can accept 
     * additional <code>Customer</code>s.
     * @return Index of the first <code>Server</code> that has an avaliable waiting 
     *     queue as an integer
     * @throws NoSuchElementException if all of the <code>Server</code>s have full waiting queues
     */
    int getFirstWaitingIndex() {
        int result = this.listOfServerStatus.indexOf(this.listOfServerStatus.stream()
                .filter(x -> x != AllServerSummary.FULL).findFirst().get());

        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

    /**
     * Finds the index of the <code>Server</code> with the shortest waiting queue. 
     * In the event of a tie-break, the index of the first-occuring <code>Server</code> is returned.
     * @return Index of the <code>Server</code> with the shortest waiting queue as an integer
     * @throws NoSuchElementException if all of the <code>Server</code>s have full waiting queues
     */
    int getShortestWaitingIndex() {
        int result = this.listOfServerStatus.indexOf(this.listOfServerStatus.stream()
                .filter(x -> x != AllServerSummary.FULL).min((x, y) -> x - y).get());
        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

}