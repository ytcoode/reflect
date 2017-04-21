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

package io.ytcode.reflect.util;

import io.ytcode.reflect.clazz.Classes;
import io.ytcode.reflect.resource.Scanner;

/** @author wangyuntao */
public class TestUtils {

  public static Classes filterInnerClasses(Class<?> c) {
    return Scanner.pkgs("io.ytcode.reflect")
        .scan()
        .pattern(".+/" + c.getSimpleName() + "\\$.+\\.class")
        .classes();
  }
}
