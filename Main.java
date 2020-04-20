import cs2030.simulator.EventSimulator;
import java.util.Scanner;

public class Main {

    public static void main(final String[] args) {
        
        final Scanner sc = new Scanner(System.in);


        int seed = sc.nextInt();
        int numOfServers = sc.nextInt();
        int numOfCheckOut = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numCustomers = sc.nextInt();
        double customerArrivalRate = sc.nextDouble();
        double customerServiceRate = sc.nextDouble();
        double restingRate = sc.nextDouble();
        double restingProbability = sc.nextDouble();
        double greedyProbability = sc.nextDouble();

        
        sc.close();

        final EventSimulator eventSimulator = EventSimulator.initialise(numOfServers, numOfCheckOut, maxQueueLength, numCustomers, seed, customerArrivalRate, customerServiceRate, restingRate, restingProbability, greedyProbability);

        eventSimulator.serviceCustomers();
    }
}