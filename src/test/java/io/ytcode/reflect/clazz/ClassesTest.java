/*
 * Copyright 2017 wangyuntao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.ytcode.reflect.clazz;

import static io.ytcode.reflect.util.TestUtils.filterInnerClasses;
import static org.junit.Assert.assertTrue;

import io.ytcode.reflect.util.TestInherited;
import io.ytcode.reflect.util.TestNotInherited;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.Test;

/** @author wangyuntao */
public class ClassesTest {

  private final Classes cs = filterInnerClasses(ClassesTest.class);

  @Test
  public void testSubTypeOf() {
    Classes cs1 = cs.subTypeOf(T1.class);
    assertTrue(cs1.size() == 1);
    assertTrue(cs1.get().asList().get(0) == T2.class);
  }

  @Test
  public void testAnnotatedWithNotInherited() {
    Classes cs1 = cs.annotatedWith(TestNotInherited.class);
    assertTrue(cs1.size() == 1);
    assertTrue(cs1.get().asList().get(0) == T1.class);
  }

  @Test
  public void testAnnotatedWithInherited() {
    Classes cs1 = cs.annotatedWith(TestInherited.class);
    assertTrue(cs1.size() == 2);

    List<Class<?>> l = cs1.get().asList();
    if (l.get(0) == T3.class) {
      assertTrue(l.get(1) == T4.class);
    } else {
      assertTrue(l.get(0) == T4.class);
      assertTrue(l.get(1) == T3.class);
    }
  }

  @Test
  public void testModifiers() {
    Classes cs1 = cs.modifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
    assertTrue(cs1.size() == 1);
    assertTrue(cs1.get().asList().get(0) == T5.class);

    Classes cs2 = cs.modifiers(Modifier.PRIVATE, Modifier.INTERFACE);
    assertTrue(cs2.size() == 1);
    assertTrue(cs2.get().asList().get(0) == T6.class);
  }

  @TestNotInherited
  private static class T1 {}

  private static class T2 extends T1 {}

  @TestInherited
  private static class T3 {}

  private static class T4 extends T3 {}

  public abstract static class T5 {}

  private interface T6 {}
}
