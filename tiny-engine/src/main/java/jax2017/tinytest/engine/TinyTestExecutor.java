package jax2017.tinytest.engine;

import org.junit.platform.commons.util.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;
import org.opentest4j.*;

public class TinyTestExecutor {

	public void execute(ExecutionRequest request, TestDescriptor descriptor) {
		if (descriptor instanceof EngineDescriptor)
			executeContainer(request, descriptor);
		if (descriptor instanceof TinyClassTestDescriptor)
			executeContainer(request, descriptor);
		if (descriptor instanceof TinyMethodTestDescriptor)
			executeMethod(request, (TinyMethodTestDescriptor) descriptor);
	}

	private void executeMethod(ExecutionRequest request, TinyMethodTestDescriptor methodTestDescriptor) {
		request.getEngineExecutionListener().executionStarted(methodTestDescriptor);
		TestExecutionResult executionResult = executeTestMethod(methodTestDescriptor);
		request.getEngineExecutionListener().executionFinished(methodTestDescriptor, executionResult);
	}

	private TestExecutionResult executeTestMethod(TinyMethodTestDescriptor methodTestDescriptor) {
		Object testInstance;
		try {
			testInstance = ReflectionUtils.newInstance(methodTestDescriptor.getTestClass());
		} catch (Throwable throwable) {
			String message = String.format( //
					"Cannot create instance of class '%s'. Maybe it has no default constructor?", //
					methodTestDescriptor.getTestClass() //
			);
			return TestExecutionResult.failed(new RuntimeException(message, throwable));
		}
		return invokeTestMethod(methodTestDescriptor, testInstance);
	}

	private TestExecutionResult invokeTestMethod(TinyMethodTestDescriptor methodTestDescriptor, Object testInstance) {
		try {

			boolean success = (boolean) ReflectionUtils.invokeMethod(methodTestDescriptor.getTestMethod(), testInstance);
			if (success)
				return TestExecutionResult.successful();
			else {
				String message = String.format( //
						"Test '%s' failed for instance '%s'", //
						methodTestDescriptor.getDisplayName(), //
						testInstance.toString() //
				);
				return TestExecutionResult.failed(new AssertionFailedError(message));
			}
		} catch (Throwable throwable) {
			return TestExecutionResult.failed(throwable);
		}
	}

	private void executeContainer(ExecutionRequest request, TestDescriptor containerDescriptor) {
		request.getEngineExecutionListener().executionStarted(containerDescriptor);
		for (TestDescriptor descriptor : containerDescriptor.getChildren()) {
			execute(request, descriptor);
		}
		request.getEngineExecutionListener().executionFinished(containerDescriptor, TestExecutionResult.successful());
	}
}
