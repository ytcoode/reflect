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

import static com.google.common.base.Preconditions.checkNotNull;
import static io.ytcode.reflect.util.Utils.predicateResourceNamePattern;
import static io.ytcode.reflect.util.Utils.predicateResourceNameSuffix;
import static io.ytcode.reflect.util.Utils.toClasses;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.clazz.Classes;
import io.ytcode.reflect.util.Filter;

/** @author wangyuntao */
public class Resources implements Supplier<ImmutableSet<Resource>>, Filter<Resource, Resources> {

  public static Resources scan(String... paths) {
    return Scanner.paths(paths).scan();
  }

  public static Resources of(ImmutableSet<Resource> resources) {
    return new Resources(resources);
  }

  private final ImmutableSet<Resource> resources;

  private Resources(ImmutableSet<Resource> resources) {
    checkNotNull(resources);
    this.resources = resources;
  }

  public Resources suffix(String suffix) {
    return filter(predicateResourceNameSuffix(suffix));
  }

  public Resources pattern(String pattern) {
    return filter(predicateResourceNamePattern(pattern));
  }

  @Override
  public Resources filter(Predicate<Resource> p) {
    return of(FluentIterable.from(resources).filter(p).toSet());
  }

  @Override
  public ImmutableSet<Resource> get() {
    return resources;
  }

  public Classes classes() {
    return Classes.of(toClasses(suffix(".class").get()));
  }
}
