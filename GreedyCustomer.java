package cs2030.simulator;

import java.util.ArrayList;



class GreedyCustomer extends Customer {

    GreedyCustomer(int id, double initialArrivalTime) {
        super(id, initialArrivalTime);
    }

    @Override
    int chooseServerForWaitIndex(ArrayList<Integer> diffFromMaxList) {
        int smallestIndex = super.chooseServerForWaitIndex(diffFromMaxList);
        int smallestQueueSize = diffFromMaxList.get(smallestIndex);
        int result = smallestIndex;
        int index = 0;
        for (Integer i : diffFromMaxList) {
            if (i < smallestQueueSize) {
                result = index;
            } else {
                index++;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "(greedy)";
    }
}