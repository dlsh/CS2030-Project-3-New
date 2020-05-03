import cs2030.simulator.EventSimulator;
import java.util.Scanner;

/**
 * A program which takes in several inputs through a <code>Scanner</code> Object and 
 * prints out the entire resulting sequence of <code>Event</code>s through an 
 * <code>EventSimulator</code> Object.
 * The inputs are:
 * <p>A seed for a <code>RandomGenerator</code> Object</p>
 * <p>The total number of <code>Servers</code> and 
 * <code>SelfCheckout</code> counters respectively</p>
 * <p>Maximum waiting queue length</p>
 * <p>Total number of <code>Customer</code>s</p>
 * <p><code>Customer</code> arrival rate - to be used to initialise the 
 * <code>RandomGenerator</code> Object</p>
 * <p><code>Customer</code> service rate - to be used to initialise the 
 * <code>RandomGenerator</code> Object</p>
 * <p><code>Server</code> resting rate - to be used to initialise the 
 * <code>RandomGenerator</code> Object</p>
 * <p><code>Server</code> cut-off resting probability</p>
 * <p>Cut-off probability that a <code>Customer</code> is greedy</p>
 */
public class Main {

    /**
     * The actual program that runs and 
     * prints out the entire sequence of <code>Event</code>s.
     * @param args The inputs taken in by the <code>Scanner</code> object.
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