package cs2030.simulator;

import java.util.ArrayList;



class GreedyCustomer extends Customer {

    GreedyCustomer(int id, double initialArrivalTime) {
        super(id, initialArrivalTime);
    }

    @Override
    int chooseServerForWaitIndex(AllServerSummary as) {
        return as.getShortestWaitingIndex();
    }

    @Override
    public String toString() {
        return super.toString() + "(greedy)";
    }
}