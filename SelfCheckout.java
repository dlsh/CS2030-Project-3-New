package cs2030.simulator;

import java.util.ArrayList;
import java.util.Optional;


class SelfCheckout extends Server {

    private final static ArrayList<Customer> sharedWaitingCustomers = new ArrayList<>();

    SelfCheckout(double serviceTime, Optional<Customer> currentlyServing, int maxQueueLength, int id) {
        super(currentlyServing, sharedWaitingCustomers, maxQueueLength, id);
    }


    static Server newEmptySelfCheckout(int maxQueueLength, int id) {
        return new SelfCheckout(0, Optional.empty(), maxQueueLength, id);
    }

    @Override
    boolean toRest(RandomGenerator rg, double restingProbability) {
        return false;
    }

    @Override
    public String toString() {
        return "self-check " + this.id;
    }
}