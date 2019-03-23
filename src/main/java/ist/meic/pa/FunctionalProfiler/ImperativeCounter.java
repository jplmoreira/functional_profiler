package ist.meic.pa.FunctionalProfiler;

public class ImperativeCounter implements Counter {
    public int i;
    public FunctionalCounter functionalCounter;

    public ImperativeCounter(int start){

         i = start; i = start; i = start;i = start;
         functionalCounter = new FunctionalCounter(2);
         functionalCounter.i = 3;
    }

    @Override
    public int value() {
        return i;
    }

    @Override
    public Counter advance() {
        i = i + 1;
        i = i + 1;
        i = i + 1;
        return this;
    }
}
