package ist.meic.pa.FunctionalProfiler;

public class ImperativeCounter implements Counter {
    private int i;

    public ImperativeCounter(int start){
        i = start;
    }

    @Override
    public int value() {
        return i;
    }

    @Override
    public Counter advance() {
        i = i + 1;
        return this;
    }
}
