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
 * Simulates a sequence of <code>Event</code>s based on several input factors.
 * <p>The total number of <code>Server</code>s and 
 * <code>SelfCheckout</code> counters respectively</p>
 * <p>Maximum waiting queue length</p>
 * <p>Total number of <code>Customer</code>s</p>
 * <p>A seed for a <code>RandomGenerator</code> object</p>
 * <p><code>Customer</code> arrival rate</p>
 * <p><code>Customer</code> service rate</p>
 * <p><code>Server</code> resting rate</p>
 * <p><code>Server</code> resting probability</p>
 * <p>Probability that a <code>Customer</code> is greedy</p>
 */
public class EventSimulator {


    /**
     * Queue of <code>Event</code>s.
     */
    private final PriorityQueue<Event> eventQueue;

    /**
     * List of all <code>Server</code>s involved in the simulation.
     */
    private final ArrayList<Server> listOfServers;

    
    /**
     * <code>RandomGenerator</code> Object to generate randomised timings.
     */
    private final RandomGenerator randomGenerator;

    /**
     * Probability of a <code>Server</code> resting.
     */
    private final double restingProbability;

    /**
     * Constructs the <code>EventSimulator</code> Object.
     * @param eventQueue The queue of <code>Event</code>s
     * @param listOfServers The list of all <code>Server</code>s involved
     * @param randomGenerator The <code>RandomGenerator</code> object
     * @param restingProbability The resting probability for all <code>Server</code>s
     */
    private EventSimulator(PriorityQueue<Event> eventQueue, ArrayList<Server> listOfServers, 
            RandomGenerator randomGenerator, double restingProbability) {
        this.eventQueue = eventQueue;
        this.listOfServers = listOfServers;
        this.randomGenerator = randomGenerator;
        this.restingProbability = restingProbability;
    }

    /**
     * Initialises the <code>EventSimulator</code> object in its starting state.
     * @param numOfServers Total number of <code>Server</code>s. 
     *     Will be added to the list of <code>Server</code>s in order of initialisation.
     * @param numOfCheckOut Total number of <code>SelfCheckout</code> counters. 
     *     Will be added to the list of <code>Server</code>s after all regular 
     *     <code>Server</code>s have been added.
     *     Will also be added in order of initialisation.
     * @param maxQueueLength Maximum waiting queue length per <code>Server</code>.
     * @param numCustomers Total number of <code>Customer</code>s. 
     *     Will be used to pre-generate arrival <code>Event</code>s 
     *     into the <code>Event</code>s queue.
     * @param seed Seed for initialising the <code>RandomGenerator</code> Object.
     * @param customerArrivalRate The <code>Customer</code> arrival rate determines 
     *     the amount of time between each <code>Customer</code>'s arrival time. 
     *     It is used in the initialisation of the <code>RandomGenerator</code>
     *     Object.
     * @param customerServiceRate The <code>Customer</code> service rate determines 
     *     the amount of time it takes for a <code>Server</code> to finish serving 
     *     this <code>Customer</code>. It is used in the initialisation of a 
     *     <code>RandomGenerator</code> Object.
     * @param restingRate The <code>Server</code>'s resting rate determines the amount of 
     *     time that the <code>Server</code> rests for. It is used in the initialisation of 
     *     the <code>RandomGenerator</code> Object.
     * @param restingProbability The probability that a <code>Server</code> will rest after 
     *     serving a <code>Customer</code>
     * @param greedyProbability The cut-off probability that determines whether a 
     *     <code>Customer</code> is a GreedyCustomer. If the generated probability 
     *     is below this <code>greedyProbability</code>, the <code>Customer</code> is 
     *     a <code>GreedyCustomer</code>
     * @return A new <code>EventSimulator</code> object with pre-generated arrival 
     *     <code>Event</code>s in its <code>Event</code>s queue 
     *     and all <code>Server</code>s and <code>SelfCheckout</code> counters stored into a list
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
     * Goes through the existing sequence of <code>Event</code>s currently stored 
     * in the <code>Event</code>s queue.
     * Simulates the entire sequence of <code>Event</code>s that occur as a result and 
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
                    eventQueue.add(e.createServingEvent(s));
                
                } catch (NoSuchElementException ex1) {
                    // When all servers cannot serve
                    try {
                        // Check and add wait event if possible
                        Server s = this.listOfServers.get(c.chooseServerForWaitIndex(summary));
                        s.addWaitingCustomer(c);
                        eventQueue.add(e.createWaitingEvent(s));

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
     * Creates a summary of all <code>Server</code>s in the list of <code>Server</code>s.
     * @return An <code>AllServerSummary</code> Object that summarises the information from 
     *     all <code>Server</code>s in the list of <code>Server</code>s.
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