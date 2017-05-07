package jax2017.tinytest.subpackage;

import org.junit.platform.commons.annotation.Testable;

@Testable
public class AClassWithoutTinyTestAnnotation {
	public boolean notATest() {
		return false;
	}
}
