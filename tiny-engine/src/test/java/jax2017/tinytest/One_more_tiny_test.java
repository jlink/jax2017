package jax2017.tinytest;

import jax2017.tinytest.engine.TinyTest;

@TinyTest
public class One_more_tiny_test {

	final int theAnswer = 42;

	public boolean this_should_return_true() {
		return theAnswer == 42;
	}

	public boolean this_returns_FALSE() {
		return theAnswer == 43;
	}
}
