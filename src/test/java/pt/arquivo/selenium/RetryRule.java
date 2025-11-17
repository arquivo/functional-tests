package pt.arquivo.selenium;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RetryRule implements TestRule {

    private final int retryCount;

    public RetryRule(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;

                for (int i = 0; i <= retryCount; i++) {
                    try {
                        System.out.println("Running test: " + description.getDisplayName() +
                                " (attempt " + (i + 1) + ")");
                        base.evaluate(); // only the test method
                        return; // success
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        System.err.println(description.getDisplayName() +
                                " failed on attempt " + (i + 1));
                    }
                }

                System.err.println(description.getDisplayName() + " failed after " + (retryCount + 1) + " attempts");
                throw caughtThrowable;
            }
        };
    }
}
