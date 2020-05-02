import cs2030.simulator.EventSimulator;
import java.util.Scanner;

/**
 * A program which takes in several inputs through a Scanner object and 
 * prints out the entire resulting sequence of events through an EventSimulator object.
 * The inputs are:
 * <p>A seed for a RandomGenerator object</p>
 * <p>The total number of Servers and SelfCheckout counters respectively</p>
 * <p>Maximum waiting queue length</p>
 * <p>Total number of customers</p>
 * <p>Customer arrival rate</p>
 * <p>Customer service rate</p>
 * <p>Server resting rate</p>
 * <p>Server resting probability</p>
 * <p>Probability that a customer is greedy</p>
 */
public class Main {

    /**
     * The actual program that runs and 
     * prints out the entire sequence of events.
     * @param args The inputs taken in by the Scanner object.
     */
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

        final EventSimulator eventSimulator = EventSimulator.initialise(numOfServers, 
                numOfCheckOut, maxQueueLength, numCustomers, seed, customerArrivalRate, 
                        customerServiceRate, restingRate, restingProbability, greedyProbability);

        eventSimulator.serviceCustomers();
    }
}