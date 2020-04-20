package cs2030.simulator;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.NoSuchElementException;




/**
 * Models a customer. A customer is initialised with ID.
 */
class Customer implements Comparable<Customer> {

    private final int id;

    private final double initialArrivalTime;

    Customer(int id, double initialArrivalTime) {
        this.id = id;
        this.initialArrivalTime = initialArrivalTime;
    }

    /**
     * Compare Customer based on their ID.
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

    int chooseServerIndex(ArrayList<Boolean> canServeList) {
        int index = 0;
        for (Boolean b : canServeList) {
            if (b) {
                return index;
            } else {
                index++;
            }
        }
        throw new NoSuchElementException("No avaliable Server");
    }

    int chooseServerForWaitIndex(ArrayList<Integer> diffFromMaxList) {
        int index = 0;
        for (Integer i : diffFromMaxList) {
            if (i < 0) {
                return index;
            } else {
                index++;
            }
        }
        throw new NoSuchElementException("No avaliable Server");
    }

    double waitingTime(double currentTime) {
        return currentTime - this.initialArrivalTime;
    }

    @Override
    public String toString() {
        return "" + this.id;
    }
}