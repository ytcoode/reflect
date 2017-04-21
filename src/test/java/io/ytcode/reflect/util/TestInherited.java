package io.ytcode.reflect.util;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.*;

/** @author wangyuntao */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
@Inherited
public @interface TestInherited {}
