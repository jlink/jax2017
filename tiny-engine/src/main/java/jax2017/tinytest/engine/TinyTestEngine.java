package jax2017.tinytest.engine;

import java.util.function.*;

import org.junit.platform.commons.support.*;
import org.junit.platform.commons.util.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.*;
import org.junit.platform.engine.support.descriptor.*;

public class TinyTestEngine implements TestEngine {

	private static final String ENGINE_ID = "tiny-test";

	private static final Predicate<Class<?>> IS_TINY_TEST_CONTAINER = classCandidate -> {
		if (ReflectionUtils.isAbstract(classCandidate))
			return false;
		if (ReflectionUtils.isPrivate(classCandidate))
			return false;
		return AnnotationSupport.isAnnotated(classCandidate, TinyTest.class);
	};

	@Override
	public String getId() {
		return ENGINE_ID;
	}

	@Override
	public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
		TestDescriptor engineDescriptor = new EngineDescriptor(uniqueId, "Tiny Test");

		request.getSelectorsByType(PackageSelector.class).forEach(selector -> {
			appendTestsInPackage(selector.getPackageName(), engineDescriptor);
		});

		request.getSelectorsByType(ClassSelector.class).forEach(selector -> {
			appendTestsInClass(selector.getJavaClass(), engineDescriptor);
		});

		return engineDescriptor;
	}

	private void appendTestsInClass(Class<?> javaClass, TestDescriptor engineDescriptor) {
		if (IS_TINY_TEST_CONTAINER.test(javaClass))
			engineDescriptor.addChild(new TinyClassTestDescriptor(javaClass, engineDescriptor));
	}

	private void appendTestsInPackage(String packageName, TestDescriptor engineDescriptor) {
		ReflectionSupport.findAllClassesInPackage(packageName, IS_TINY_TEST_CONTAINER, name -> true) //
				.stream() //
				.map(aClass -> new TinyClassTestDescriptor(aClass, engineDescriptor)) //
				.forEach(engineDescriptor::addChild);
	}

	@Override
	public void execute(ExecutionRequest request) {
		TestDescriptor root = request.getRootTestDescriptor();
		new TinyTestExecutor().execute(request, root);
	}

}
