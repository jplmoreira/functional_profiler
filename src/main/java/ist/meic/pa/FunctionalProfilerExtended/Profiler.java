package ist.meic.pa.FunctionalProfilerExtended;

import java.util.*;

public class Profiler {
    private static HashMap<String, ProfilerData> writers = new HashMap<>();
    private static HashMap<String, ProfilerData> readers = new HashMap<>();

    public static void incrementWriter(String className, String methodName) {
        if(writers.containsKey(className))
           writers.get(className).incrementAccess(methodName);
        else
           writers.put(className, new ProfilerData(methodName));
    }

    public static void incrementReader(String className, String methodName) {
        if(readers.containsKey(className))
            readers.get(className).incrementAccess(methodName);
        else
           readers.put(className, new ProfilerData(methodName));
    }

    public static void printStatus() {
        Set<String> classKeys = new TreeSet<>();
        classKeys.addAll(writers.keySet());
        classKeys.addAll(readers.keySet());

        int totalReads = readers.values().stream().mapToInt(ProfilerData::getAccessCount).sum();
        int totalWrites = writers.values().stream().mapToInt(ProfilerData::getAccessCount).sum();
        System.out.println("Total reads: " + totalReads + " Total writes: " + totalWrites);
        for (String className : classKeys) {
            System.out.println(getClassProfile(className));

            Set<String> methodKeys = new TreeSet<>();
            if(readers.containsKey(className))
                methodKeys.addAll(readers.get(className).getMethodData().keySet());
            if(writers.containsKey(className))
                methodKeys.addAll(writers.get(className).getMethodData().keySet());
            for (String methodName : methodKeys){
                System.out.println(getMethodProfile(className, methodName));
            }

            System.out.println();
        }
    }

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
