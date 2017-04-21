package io.ytcode.reflect.util;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @author wangyuntao */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface TestNotInherited {}
