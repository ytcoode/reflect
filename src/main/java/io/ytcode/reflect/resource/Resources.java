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

import static io.ytcode.reflect.util.Utils.predicateResourceNamePattern;
import static io.ytcode.reflect.util.Utils.predicateResourceNameSuffix;
import static io.ytcode.reflect.util.Utils.toClasses;

import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.Filterable;
import io.ytcode.reflect.clazz.Classes;

/** @author wangyuntao */
public class Resources extends Filterable<Resource, Resources> {

  public static Resources of(ImmutableSet<Resource> resources) {
    return new Resources(resources);
  }

  private Resources(ImmutableSet<Resource> resources) {
    super(resources);
  }

  public Resources suffix(String suffix) {
    return filter(predicateResourceNameSuffix(suffix));
  }

  public Resources pattern(String pattern) {
    return filter(predicateResourceNamePattern(pattern));
  }

  public Classes classes() {
    return Classes.of(toClasses(suffix(".class")));
  }
}
