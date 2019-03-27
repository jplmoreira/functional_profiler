package ist.meic.pa.FunctionalProfilerExtended;

import java.util.HashMap;
import java.util.Map;

public class ProfilerData {
    private Map<String, Integer> methodData = new HashMap<>();
    private int accessCount = 0;

    public ProfilerData(String methodName){
                //System.out.println("\t >> ProfilerData Constructor");
        incrementAccess(methodName);
    }

    public void incrementAccess(String methodName) {
        //System.out.println("\t >> incrementAccess");
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
