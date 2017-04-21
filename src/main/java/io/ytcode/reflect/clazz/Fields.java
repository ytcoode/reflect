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

import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Field;

/** @author wangyuntao */
public class Fields extends Members<Field, Fields> {

  public static Fields of(ImmutableSet<Field> fields) {
    return new Fields(fields);
  }

  private Fields(ImmutableSet<Field> fields) {
    super(fields);
  }
}
