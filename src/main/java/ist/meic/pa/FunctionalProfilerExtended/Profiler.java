package ist.meic.pa.FunctionalProfilerExtended;

import java.util.*;

public class Profiler {
    private static HashMap<String, ProfilerData> writers = new HashMap<>();  // Counters for classes are static Hashmaps with class names as keys and the class ProfilerData as the value
    private static HashMap<String, ProfilerData> readers = new HashMap<>();

    /*******************************************************
     * This is a static method for incrementing the class writer counter.
     * It verifies if the hashmap contains the key and, if it does,
     * increment the counter otherwise puts the key on the map with value 1
     *******************************************************/
    public static void incrementWriter(String className, String methodName) {
        if(writers.containsKey(className))
           writers.get(className).incrementAccess(methodName);
        else
           writers.put(className, new ProfilerData(methodName));
    }

    /*******************************************************
     * This is a static method for incrementing the class reader counter.
     * It verifies if the hashmap contains the key and, if it does,
     * increment the counter otherwise puts the key on the map with value 1
     *******************************************************/
    public static void incrementReader(String className, String methodName) {
        if(readers.containsKey(className))
            readers.get(className).incrementAccess(methodName);
        else
           readers.put(className, new ProfilerData(methodName));
    }

    /*******************************************************
     * This is a static method for printing the final results of the writing and reading counters.
     *******************************************************/
    public static void printStatus() {
        Set<String> classKeys = new TreeSet<>();
        classKeys.addAll(writers.keySet());       // Add all the keys to an ordered set
        classKeys.addAll(readers.keySet());

        int totalReads = readers.values().stream().mapToInt(ProfilerData::getAccessCount).sum();
        int totalWrites = writers.values().stream().mapToInt(ProfilerData::getAccessCount).sum();
        System.out.println("Total reads: " + totalReads + " Total writes: " + totalWrites); // Print the total sums
        for (String className : classKeys) {
            System.out.println(getClassProfile(className));                                 // Print the information about each class

            Set<String> methodKeys = new TreeSet<>();
            if(readers.containsKey(className))
                methodKeys.addAll(readers.get(className).getMethodData().keySet());
            if(writers.containsKey(className))
                methodKeys.addAll(writers.get(className).getMethodData().keySet());
            for (String methodName : methodKeys){                                           // Cycle through all of the methods of this class
                System.out.println(getMethodProfile(className, methodName));                // Print the information of each method
            }

            System.out.println();
        }
    }

    /*******************************************************
     * This is a method to print the number of reads and writes on a method of a class
     *******************************************************/
    private static String getMethodProfile(String className, String methodName) {
        String result = "\treads: ";
        if(readers.containsKey(className))
            result += readers.get(className).getMethodData().getOrDefault(methodName, 0);
        else
            result += "0";
        result += " writes: ";
        if(writers.containsKey(className))
            result += writers.get(className).getMethodData().getOrDefault(methodName, 0);
        else
            result += "0";
        result +=  " on method " + methodName;
        return result;
    }

    /*******************************************************
     * This is a method to print the number of reads and writes on a class
     *******************************************************/
    private static String getClassProfile(String className) {
        String result = "class " + className + " -> reads: ";
        ProfilerData readerData = readers.get(className);
        result += readerData != null ? readerData.getAccessCount() : 0;
        result += " writes: ";
        ProfilerData writerData = writers.get(className);
        result += writerData != null ? writerData.getAccessCount() : 0;
        return result;
    }
}
