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

package io.ytcode.reflect.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** @author wangyuntao */
public class ResourcesTest {

  private final Resources resources = Scanner.pkgs("io.ytcode.reflect").scan();

  @Test
  public void testPattern() {
    Resources r = resources.pattern(".*/Resources\\.class");
    assertTrue(r.size() == 1);
    assertEquals(r.classes().get().asList().get(0), Resources.class);
  }

  @Test
  public void testClasses() {
    assertTrue(resources.classes().size() > 0);
  }
}
