package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;


/**
 * Simulates a sequence of events based on several input factors.
 * <p>The total number of Servers and SelfCheckout counters respectively</p>
 * <p>Maximum waiting queue length</p>
 * <p>Total number of customers</p>
 * <p>A seed for a RandomGenerator object</p>
 * <p>Customer arrival rate</p>
 * <p>Customer service rate</p>
 * <p>Server resting rate</p>
 * <p>Server resting probability</p>
 * <p>Probability that a customer is greedy</p>
 */
public class EventSimulator {


    /**
     * Queue of Events.
     */
    private final PriorityQueue<Event> eventQueue;

    /**
     * List of Servers. Determined by number of servers.
     */
    private final ArrayList<Server> listOfServers;

    
    /**
     * RandomGenerator Object.
     */
    private final RandomGenerator randomGenerator;

    /**
     * Probability of Server resting.
     */
    private final double restingProbability;

    /**
     * Constructs the EventSimulator Object.
     * @param eventQueue The queue of events
     * @param listOfServers The list of all Servers involved
     * @param randomGenerator The RandomGenerator object
     * @param restingProbability The resting probability for all Servers
     */
    private EventSimulator(PriorityQueue<Event> eventQueue, ArrayList<Server> listOfServers, 
            RandomGenerator randomGenerator, double restingProbability) {
        this.eventQueue = eventQueue;
        this.listOfServers = listOfServers;
        this.randomGenerator = randomGenerator;
        this.restingProbability = restingProbability;
    }

    /**
     * Initialises the EventSimulator object in its starting state.
     * @param numOfServers Total number of Servers. 
     *     Will be added to the list of Servers in order.
     * @param numOfCheckOut Total number of SelfCheckout counters. 
     *     Will be added to the list of Servers after all regular Servers have been added.
     * @param maxQueueLength Maximum waiting queue length per Server.
     * @param numCustomers Total number of customers. 
     *     Will be used to pre-generate arrival events into the event queue.
     * @param seed Seed for the RandomGenerator object.
     * @param customerArrivalRate The customer arrival rate determines 
     *     the amount of time between each customer's arrival time.
     * @param customerServiceRate The customer service rate determines 
     *     the amount of time it takes for a Server to finish serving this customer.
     * @param restingRate The Server's resting rate determines the amount of 
     *     time that the Server rests for.
     * @param restingProbability The probability that a Server will rest after serving a customer.
     * @param greedyProbability The probability that a Customer is a GreedyCustomer.
     * @return A new EventSimulator object with pre-generated arrival events in its event queue 
     *     and all Servers and SelfCheckout counters stored into a list.
     */
    public static EventSimulator initialise(int numOfServers, int numOfCheckOut, 
            int maxQueueLength, int numCustomers, int seed, double customerArrivalRate, 
                    double customerServiceRate, double restingRate, double restingProbability, 
                            double greedyProbability) {

        // Initialise helper classes
        RandomGenerator randomGenerator = new RandomGenerator(seed, customerArrivalRate, 
                customerServiceRate, restingRate);

        // Initialise and add Servers to a list
        ArrayList<Server> listOfServers = new ArrayList<>();
        IntStream.rangeClosed(1, numOfServers + numOfCheckOut).forEachOrdered(x -> {
            if (x <= numOfServers) {
                listOfServers.add(Server.newEmptyServer(maxQueueLength, x));
            } else {
                listOfServers.add(SelfCheckout.newEmptySelfCheckout(maxQueueLength, x));
            }
        });

        // Generate all arrival timings
        double[] allArrivalTimings = DoubleStream.iterate(0, x -> x + 
                randomGenerator.genInterArrivalTime()).limit(numCustomers).toArray();
        
        // Initialise PriorityQueue for Events and add them into the PQ
        PriorityQueue<Event> eventQueue = new PriorityQueue<>();
        IntStream.rangeClosed(1, numCustomers).mapToObj(x -> {
            if (randomGenerator.genCustomerType() < greedyProbability) {
                return Event.createArrivingEvent(allArrivalTimings[x - 1], x, true);
            } else {
                return Event.createArrivingEvent(allArrivalTimings[x - 1], x, false);
            }
        }).forEachOrdered(x -> eventQueue.add(x));;


        return new EventSimulator(eventQueue, listOfServers, randomGenerator, restingProbability);
    }

    /**
     * Goes through the existing sequence of events currently stored in the events queue.
     * Simulates the entire sequence of events that occur as a result and 
     * prints out this entire sequence.
     */
    public void serviceCustomers() {
        // Initial statistics
        double totalWaitingTime = 0;
        int customersLeft = 0;
        int customersServed = 0;

        // Loop through priority queue until all events are done
        while (!this.eventQueue.isEmpty()) {
            Event e = eventQueue.poll();

            // Handle arrival events
            if (e.isArrival()) {
                Customer c = e.getCustomerDetails();
                AllServerSummary summary = this.createSummary();

                try {
                    // Check and add serve event if possible
                    Server s = this.listOfServers.get(c.chooseServerIndex(summary));
                    s.serveCustomer(c);
                    eventQueue.add(e.createServingEvent(c, s));
                
                } catch (NoSuchElementException ex1) {
                    // When all servers cannot serve
                    try {
                        // Check and add wait event if possible
                        Server s = this.listOfServers.get(c.chooseServerForWaitIndex(summary));
                        s.addWaitingCustomer(c);
                        eventQueue.add(e.createWaitingEvent(c, s));

                    } catch (NoSuchElementException ex2) {
                        // If unable to find server & unable to find queue, customer leaves
                        eventQueue.add(e.createLeavingEvent());
                        customersLeft++;
                    }

                }

            } else if (e.isServe()) {
                // Only generate the service time when customer is served
                double serviceTime = this.randomGenerator.genServiceTime();
                eventQueue.add(e.createDoneEvent(serviceTime));
                customersServed++;

            } else if (e.isDone() | e.isServerRest()) {
                Server s = e.getServerDetails();
                boolean completedProcess = false;

                // Only check for rest possibility for Done events
                if (e.isDone()) {
                    if (s.toRest(this.randomGenerator, restingProbability)) {
                        // Generate server rest event
                        double restTime = this.randomGenerator.genRestPeriod();
                        eventQueue.add(e.createServerRestEvent(restTime));
                        completedProcess = true;
                    }
                }

                if (!completedProcess) {
                    if (s.hasWaitingCustomer()) {
                        // Generate serve event for Customer in waiting queue
                        Customer waitingCustomer = s.serveWaitingCustomer();
                        Event serveForWait = e.createServingEvent(waitingCustomer, s);
                        eventQueue.add(serveForWait);
                        totalWaitingTime += serveForWait.getCustomerWaitingTime();

                    } else {
                        // If no waiting customers, clear server
                        s.clear();
                    }
                }
            }
            // Print out events
            if (!e.isServerRest()) {
                System.out.println(e);
            }
        }

        String averageWaitingTime = "0.000";
        if (customersServed != 0) {
            averageWaitingTime = String.format("%.3f", totalWaitingTime / customersServed);
        }
        String finalStats = "[" + averageWaitingTime + 
                " " + customersServed + " " + customersLeft + "]";
        System.out.println(finalStats);
    }



    /**
     * Creates a summary of all Servers in the list of Servers.
     * @return An AllServerSummary object that summarises the information from 
     *     all Servers in the list of Servers.
     */
    private AllServerSummary createSummary() {
        AllServerSummary as = new AllServerSummary();
        this.listOfServers.stream().forEachOrdered(x -> {
            if (x.canServe()) {
                as.addStatus(AllServerSummary.AVALIABLE);
            } else if (!x.canWait()) {
                as.addStatus(AllServerSummary.FULL);
            } else {
                as.addStatus(x.waitingQueueSize());
            }
        });
        return as;
    }

    
}