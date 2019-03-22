package ist.meic.pa.FunctionalProfiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProfilerMaps {
    private static Map<String, Integer> writers = new HashMap<>();
    private static Map<String, Integer> readers = new HashMap<>();

    public static void incrementWriter(String className) {
        if(writers.containsKey(className)){
           writers.put(className, writers.get(className) + 1);
        } else
           writers.put(className, 1);
    }

    public static void incrementReader(String className) {
        if(readers.containsKey(className)){
           readers.put(className, readers.get(className) + 1);
        } else
           readers.put(className, 1);
    }

    public static void printStatus() {
        Set<String> keys = new HashSet<String>();
        keys.addAll(writers.keySet());
        keys.addAll(readers.keySet());
        int totalReads = readers.values().stream().mapToInt(Integer::intValue).sum();
        int totalWrites = writers.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total reads: " + totalReads + " Total writes: " + totalWrites);
        for (String className : keys) {
            String result = "class " + className + " -> reads: ";
            result += (readers.containsKey(className)) ? readers.get(className) : 0;
            result += " writes: ";
            result += (writers.containsKey(className)) ? writers.get(className) : 0;
            System.out.println(result);
        }
    }
}
