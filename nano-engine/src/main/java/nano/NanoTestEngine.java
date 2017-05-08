package nano;

import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;

public class NanoTestEngine implements TestEngine {

	@Override
	public String getId() {
		return "nano";
	}

	@Override
	public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId engineId) {
		EngineDescriptor engine = new EngineDescriptor(engineId, "JAX 2017 Nano Engine");
		engine.addChild(new NanoTestDescriptor(engineId, "test1"));
		engine.addChild(new NanoTestDescriptor(engineId, "test2"));
		engine.addChild(new NanoTestDescriptor(engineId, "test3"));
		return engine;
	}

	@Override
	public void execute(ExecutionRequest request) {
		TestDescriptor engineDescriptor = request.getRootTestDescriptor();
		EngineExecutionListener listener = request.getEngineExecutionListener();

		listener.executionStarted(engineDescriptor);
		for (TestDescriptor testDescriptor : engineDescriptor.getChildren()) {
			listener.executionStarted(testDescriptor);
			if (testDescriptor.getDisplayName().contains("2"))
				listener.executionFinished(testDescriptor, TestExecutionResult.failed(new AssertionError("test should not contain 2")));
			else listener.executionFinished(testDescriptor, TestExecutionResult.successful());
		}
		listener.executionFinished(engineDescriptor, TestExecutionResult.successful());

	}
}
