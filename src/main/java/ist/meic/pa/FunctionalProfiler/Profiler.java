package ist.meic.pa.FunctionalProfiler;


import java.util.*;

public class Profiler {
    private static HashMap<String, Integer> writers = new HashMap<>();  // Counters for classes are static Hashmaps with class names as keys and the counter itself as the value
    private static HashMap<String, Integer> readers = new HashMap<>();

    /*******************************************************
     * This is a static method for incrementing the class writer counter.
     * It verifies if the hashmap contains the key and, if it does,
     * increment the counter otherwise puts the key on the map with value 1
     *******************************************************/
    public static void incrementWriter(String className) {
        if(writers.containsKey(className))
            writers.put(className, writers.get(className) + 1);
        else
           writers.put(className, 1);
    }

    /*******************************************************
     * This is a static method for incrementing the class reader counter.
     * It verifies if the hashmap contains the key and, if it does,
     * increment the counter otherwise puts the key on the map with value 1
     *******************************************************/
    public static void incrementReader(String className) {
        if(readers.containsKey(className)){
           readers.put(className, readers.get(className) + 1);
        } else
           readers.put(className, 1);
    }

    /*******************************************************
     * This is a static method for printing the final results of the writing and reading counters.
     *******************************************************/
    public static void printStatus() {
        Set<String> keys = new TreeSet<>();  // Create a tree set of strings to use the natural ordering
        keys.addAll(writers.keySet());       // Add all the keys of both counters to the set 
        keys.addAll(readers.keySet());       // Sets can only have unique values eliminating the problem of repeating keys on both counters

        int totalReads = readers.values().stream().mapToInt(Integer::intValue).sum();  // Sum all the values of the reader counter map using 'map' method
        int totalWrites = writers.values().stream().mapToInt(Integer::intValue).sum(); // Sum all the values of the writer counter map using 'map' method
        System.out.println("Total reads: " + totalReads + " Total writes: " + totalWrites); // Print the total sums
        for (String className : keys) {                                                     // Cycle through all the keys (class names) and print the reads and writes of each one
            String result = "class " + className + " -> reads: ";
            result += readers.getOrDefault(className, 0);
            result += " writes: ";
            result += writers.getOrDefault(className, 0);
            System.out.println(result);
        }
    }
}
