package cs2030.simulator;




class GreedyCustomer extends Customer {

    GreedyCustomer(int id, double initialArrivalTime) {
        super(id, initialArrivalTime);
    }



    @Override
    public String toString() {
        return super.toString() + "(greedy)";
    }
}