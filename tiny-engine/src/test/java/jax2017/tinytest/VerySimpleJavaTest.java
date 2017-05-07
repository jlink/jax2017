package jax2017.tinytest;

import jax2017.tinytest.engine.TinyTest;
import org.opentest4j.AssertionFailedError;

@TinyTest
class VerySimpleJavaTest {

    boolean success() {
        return true;
    }

    Boolean boxedBooleanSuccess() {
        return true;
    }

    boolean two_numbers_are_not_equal() {
        return 34 == 35;
    }

    boolean fail_with_AssertViolation() {
    	throw new AssertionFailedError("Should be equal!", 34, 35);
	}

    void voidShouldNotBeDiscovered() {

    }

    boolean withParametersShouldNotBeDiscovered(String test) {
        return false;
    }

    private boolean privateShouldNotBeDiscovered() {
        return false;
    }

    static boolean staticShouldNotBeDiscovered() {
    	return false;
	}
}
