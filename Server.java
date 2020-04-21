package cs2030.simulator;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Models a Server that is mutable. A server has the service time, Customer thaty it is serving, and a waiting queue.
 */
class Server {


    /**
     * The Customer that the Server is currently serving.
     */
    private Optional<Customer> currentlyServing;

    /**
     * List of waiting customers.
     */
    private ArrayList<Customer> waitingCustomers;

    /**
     * Maximum length of the waiting queue. 
     */
    private int maxQueueLength;

    /**
     * Identify each Server.
     */
    protected int id;

    protected Server(Optional<Customer> currentlyServing, ArrayList<Customer> waitingCustomers, int maxQueueLength, int id) {

        this.currentlyServing = currentlyServing;
        this.waitingCustomers = waitingCustomers;
        this.maxQueueLength = maxQueueLength;
        this.id = id;
    }

    /**
     * Create a new empty server.
     */
    static Server newEmptyServer(int maxQueueLength, int id) {
        ArrayList<Customer> waitingCustomers = new ArrayList<Customer>();
        return new Server(Optional.empty(), waitingCustomers, maxQueueLength, id);
    }

    boolean canServe() {
        return this.currentlyServing.isEmpty();
    }

    void serveCustomer(Customer c) {
        this.currentlyServing = Optional.ofNullable(c);
    }

    int diffFromMax() {
        return this.waitingCustomers.size() - this.maxQueueLength;
    }

    int waitingQueueSize() {
        return this.waitingCustomers.size();
    }

    boolean canWait() {
        return this.waitingCustomers.size() < this.maxQueueLength;
    }

    void addWaitingCustomer(Customer c) {
        this.waitingCustomers.add(c);
    }

    Customer serveWaitingCustomer() {
        Customer c = this.waitingCustomers.remove(0);
        this.currentlyServing = Optional.ofNullable(c);
        return c;
    }

    boolean hasWaitingCustomer() {
        return this.waitingCustomers.size() > 0;
    }

    boolean hasShorterQueue(Server s) {
        return this.waitingCustomers.size() < s.waitingCustomers.size();
    }

    boolean toRest(RandomGenerator rg, double restingProbability) {
        return (rg.genRandomRest() < restingProbability);
    }

    void clear() {
        this.currentlyServing = Optional.empty();
    }

    @Override
    public String toString() {
        return "server " + this.id;
    }
}