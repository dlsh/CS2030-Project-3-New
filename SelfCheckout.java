package cs2030.simulator;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A <code>SelfCheckout</code> inherits from <code>Server</code>. The only difference is 
 * that it stores its waiting customers in a static <code>ArrayList</code>.
 * This allows for all <code>SelfCheckout</code> counters to be able to access and 
 * modify the same waiting queue, resulting in a shared queue.
 */
class SelfCheckout extends Server {

    /**
     * The shared waiting queue for <code>SelfCheckout</code> counters.
     */
    private static final ArrayList<Customer> sharedWaitingCustomers = new ArrayList<>();

    /**
     * Constructs the <code>SelfCheckout</code> counter using the following parameters.
     * @param currentlyServing The <code>Customer</code> that is currently being served
     * @param maxQueueLength Maximum length of the shared waiting queue
     * @param id A unique integer identifier
     */
    private SelfCheckout(Optional<Customer> currentlyServing, 
            int maxQueueLength, int id) {
        super(currentlyServing, sharedWaitingCustomers, maxQueueLength, id);
    }

    /**
     * Initialises a new empty <code>SelfCheckout</code> object.
     * @param maxQueueLength The maximum length of the shared 
     *     waiting queue
     * @param id The integer identifier for the <code>SelfCheckout</code> counter
     * @return A new <code>SelfCheckout</code> counter with the unique integer ID 
     *     and the specified maximum waiting queue length
     */
    static Server newEmptySelfCheckout(int maxQueueLength, int id) {
        return new SelfCheckout(Optional.empty(), maxQueueLength, id);
    }

    @Override
    /**
     * Always returns false as a <code>SelfCheckout</code> counter does not rest.
     */
    boolean toRest(RandomGenerator rg, double restingProbability) {
        return false;
    }

    /**
     * Converts the <code>SelfCheckout</code>'s ID to string format.
     * @return The <code>SelfCheckout</code>'s ID prefixed with "self-check" as a string.
     */
    @Override
    public String toString() {
        return "self-check " + this.id;
    }
}