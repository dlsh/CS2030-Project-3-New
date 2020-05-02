package cs2030.simulator;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Models a <code>Server</code> that is mutable. 
 * A <code>Server</code> has its ID, <code>Customer</code> that it is serving, 
 * a waiting queue and a max waiting queue length.
 * The <code>Server</code> is made mutable so that it can be immediately updated 
 * when there are changes to the <code>Customer</code> it is serving, 
 * or its waiting queue.
 * The <code>Customer</code> that it is serving is encapsulated by an Optional - 
 * there are cases where the <code>Server</code> is not serving anyone.
 */
class Server {


    /**
     * The <code>Customer</code> that the <code>Server</code> is currently serving.
     */
    private Optional<Customer> currentlyServing;

    /**
     * List of waiting <code>Customer</code>s.
     */
    private ArrayList<Customer> waitingCustomers;

    /**
     * Maximum length of the waiting queue.
     */
    private int maxQueueLength;

    /**
     * A unique integer identifier for each <code>Server</code>.
     */
    protected int id;

    /**
     * Constructs the <code>Server</code> based on the following input parameters.
     * @param currentlyServing The <code>Customer</code> that is currently being served
     * @param waitingCustomers The waiting queue
     * @param maxQueueLength The maximum length of the waiting queue
     * @param id The unique integer identifier
     */
    protected Server(Optional<Customer> currentlyServing, 
            ArrayList<Customer> waitingCustomers, int maxQueueLength, int id) {

        this.currentlyServing = currentlyServing;
        this.waitingCustomers = waitingCustomers;
        this.maxQueueLength = maxQueueLength;
        this.id = id;
    }

    /**
     * Create a new empty <code>Server</code> with an empty waiting queue and 
     * an empty <code>Optional</code> for currently serving <code>Customer</code> 
     * to represent no <code>Customer</code> currently being served.
     * @param maxQueueLength Maximum length of the waiting queue
     *     for this <code>Server</code>
     * @param id An integer identifier for this Server
     * @return A new <code>Server</code> with the unique integer ID and the 
     *     specified maximum waiting queue length
     */
    static Server newEmptyServer(int maxQueueLength, int id) {
        ArrayList<Customer> waitingCustomers = new ArrayList<Customer>();
        return new Server(Optional.empty(), waitingCustomers, maxQueueLength, id);
    }

    /**
     * Checks whether the <code>Server</code> can serve a <code>Customer</code>.
     * @return <code>true</code> if the <code>Server</code> is not currently 
     *     serving a <code>Customer</code>, otherwise <code>false</code>
     */
    boolean canServe() {
        return this.currentlyServing.isEmpty();
    }

    /**
     * Takes in a <code>Customer</code> and updates the <code>Server</code> 
     * to be serving this <code>Customer</code>.
     * @param c The <code>Customer</code> that is being served by this <code>Server</code>.
     */
    void serveCustomer(Customer c) {
        this.currentlyServing = Optional.ofNullable(c);
    }

    /**
     * Shows the difference between the size of the waiting queue and the maximum queue length.
     * @return Size of waiting queue - maximum queue length
     */
    int diffFromMax() {
        return this.waitingCustomers.size() - this.maxQueueLength;
    }

    /**
     * Finds the size of the waiting queue.
     * @return The size of the waiting queue as an integer value
     */
    int waitingQueueSize() {
        return this.waitingCustomers.size();
    }

    /**
     * Checks whether the <code>Server</code> can take in a 
     * <code>Customer</code> in its waiting queue.
     * @return <code>true</code> if the length of the waiting queue is 
     *     shorter than the maximum waiting queue length, otherwise 
     *     <code>false</code>
     */
    boolean canWait() {
        return this.waitingCustomers.size() < this.maxQueueLength;
    }

    /**
     * Adds a <code>Customer</code> to its waiting queue.
     * @param c The <code>Customer</code> that is to be added to this
     *     <code>Server</code>'s waiting queue
     */
    void addWaitingCustomer(Customer c) {
        this.waitingCustomers.add(c);
    }

    /**
     * Serves the first <code>Customer</code> in the waiting queue.
     * @return The <code>Customer</code> that was previously waiting 
     *     and is now being served.
     */
    Customer serveWaitingCustomer() {
        Customer c = this.waitingCustomers.remove(0);
        this.serveCustomer(c);
        return c;
    }

    /**
     * Checks whether the waiting queue has <code>Customers</code>.
     * @return <code>true</code> if the waiting queue has at least 
     *     1 <code>Customer</code>, otherwise <code>false</code>
     */
    boolean hasWaitingCustomer() {
        return this.waitingCustomers.size() > 0;
    }

    /**
     * Checks whether the <code>Server</code> rests based on a 
     * <code>RandomGenerator</code> object 
     * and a given resting probability.
     * @param rg The <code>RandomGenerator</code> object
     * @param restingProbability The cut-off probability that determines 
     *     whether the <code>Server</code> rests.
     * @return <code>true</code> if the generated probability 
     *     is less than the provided <code>restingProbability</code>
     */
    boolean toRest(RandomGenerator rg, double restingProbability) {
        return (rg.genRandomRest() < restingProbability);
    }

    /**
     * Clears the currently being served <code>Customer</code> from 
     * the <code>Server</code>.
     */
    void clear() {
        this.currentlyServing = Optional.empty();
    }

    /**
     * Converts the <code>Server</code>'s ID to string format.
     * @return The <code>Server</code>'s ID prefixed with "server" 
     *     in a string format.
     */
    @Override
    public String toString() {
        return "server " + this.id;
    }
}