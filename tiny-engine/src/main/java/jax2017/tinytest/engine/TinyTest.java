package jax2017.tinytest.engine;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Testable
public @interface TinyTest {
}
