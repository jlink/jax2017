package jax2017.tinytest.engine;

import org.junit.platform.commons.util.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;

import java.lang.reflect.*;
import java.util.function.*;

class TinyClassTestDescriptor extends AbstractTestDescriptor {
	private final Class<?> testClass;

	public TinyClassTestDescriptor(Class<?> testClass, TestDescriptor parent) {
		super( //
				parent.getUniqueId().append("class", testClass.getName()), //
				determineDisplayName(testClass), //
				ClassSource.from(testClass) //
		);
		this.testClass = testClass;
		setParent(parent);
		addAllChildren();
	}

	private static String determineDisplayName(Class testClass) {
		return DisplayName.canonize(testClass.getSimpleName());
	}

	private void addAllChildren() {
		Predicate<Method> isTestMethod = method -> {
			if (ReflectionUtils.isStatic(method))
				return false;
			if (ReflectionUtils.isPrivate(method))
				return false;
			if (ReflectionUtils.isAbstract(method))
				return false;
			if (method.getParameterCount() > 0)
				return false;
			return method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class);
		};
		ReflectionUtils.findMethods(testClass, isTestMethod).stream() //
				.map(method -> new TinyMethodTestDescriptor(method, testClass, this)) //
				.forEach(this::addChild);
	}

	@Override
	public Type getType() {
		return Type.CONTAINER;
	}
}
