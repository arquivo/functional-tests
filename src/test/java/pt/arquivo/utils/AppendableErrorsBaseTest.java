package pt.arquivo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Ignore;

@Ignore
public abstract class AppendableErrorsBaseTest {

	private List<Throwable> verificationErrors = new ArrayList<Throwable>();

	/**
	 * Releases the resources used for the tests, i.e., It closes the WebDriver.
	 */
	@After
	public void tearDown() throws Exception {
		if (!verificationErrors.isEmpty()) {
			String errorMessageForAllErrors = verificationErrors.stream() //
					.map(e -> e.getLocalizedMessage()) //
					.collect(Collectors.joining(System.lineSeparator()));
			Throwable firstError = verificationErrors.iterator().next();
			throw new AssertionError(errorMessageForAllErrors, firstError);
		}

		System.out.println(String.format("End running test: %s\n", this.getClass().getSimpleName()));
	}

	protected void run(String errorMessage, Runnable r) {
		try {
			r.run();
		} catch (Throwable t) {
			throw new Error(errorMessage, t);
		}
	}

	protected <T> T run(String errorMessage, Callable<T> c) {
		try {
			return (T) c.call();
		} catch (Throwable t) {
			throw new Error(errorMessage, t);
		}
	}

	protected void appendError(String errorMessage, Runnable r) {
		try {
			r.run();
		} catch (Throwable e) {
			// print now the stack trace, because at the end could be a list of errors.
			e.printStackTrace();

			verificationErrors.add(new Error(errorMessage, e));
		}
	}


	protected void appendError(Runnable r) {
		try {
			r.run();
		} catch (Throwable e) {
			// print now the stack trace, because at the end could be a list of errors.
			e.printStackTrace();

			verificationErrors.add(e);
		}
	}

}
