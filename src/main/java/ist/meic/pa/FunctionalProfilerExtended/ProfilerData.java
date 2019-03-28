package ist.meic.pa.FunctionalProfilerExtended;

import java.util.HashMap;
import java.util.Map;

/*******************************************************
 * This class is to be used as the counter for the writer and reader hash map 
 *******************************************************/
public class ProfilerData {
    private Map<String, Integer> methodData = new HashMap<>(); // Uses a hash map to count the number of accesses on each method of a class (reader or writer, depending on where the object is located)
    private int accessCount = 0;                               // Total counter of accesses of a class (reader or writer, depending on where the object is located)

    public ProfilerData(String methodName){
        incrementAccess(methodName);
    }

    public void incrementAccess(String methodName) {
        if(methodData.containsKey(methodName))
            methodData.put(methodName, methodData.get(methodName) + 1);
        else
            methodData.put(methodName, 1);
        accessCount++;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public  Map<String, Integer> getMethodData() {
        return methodData;
    }
}
