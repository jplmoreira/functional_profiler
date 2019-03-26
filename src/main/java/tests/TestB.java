package tests;

import tests.domain.Counter;
import tests.domain.FunctionalCounter;
import tests.domain.ImperativeCounterModified;

public class TestB {
    public static void test(Counter c1, Counter c2) {
        System.out.println(String.format("%s %s", c1.value(), c2.value()));
    }
    public static void main(String[] args) {
        Counter fc = new FunctionalCounter(0);
        test(fc, fc.advance());
        Counter ic = new ImperativeCounterModified(0);
        test(ic, ic.advance());
    }
}
