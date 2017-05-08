package nano;

import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;

public class NanoTestDescriptor extends AbstractTestDescriptor {

	protected NanoTestDescriptor(UniqueId engineId, String testName) {
		super(engineId.append("nano", testName), testName);
	}

	@Override
	public Type getType() {
		return Type.TEST;
	}
}
