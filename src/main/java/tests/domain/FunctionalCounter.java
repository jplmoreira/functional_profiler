package tests.domain;

public class FunctionalCounter implements Counter {
    public int i;

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
