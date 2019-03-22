package ist.meic.pa.FunctionalProfiler;

public class FunctionalCounter implements Counter {
   private int i;

    public FunctionalCounter(int start) {
        i = start;
    }

    @Override
    public int value() {
        return i;
    }

    @Override
    public Counter advance() {
        return new FunctionalCounter(i + 1);
    }
}
