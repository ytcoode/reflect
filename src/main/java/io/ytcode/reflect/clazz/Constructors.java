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

import static com.google.common.base.Preconditions.checkNotNull;
import static io.ytcode.reflect.util.Utils.predicateConstructorAnnotatedWith;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.util.Filter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/** @author wangyuntao */
public class Constructors
    implements Supplier<ImmutableSet<Constructor<?>>>, Filter<Constructor<?>, Constructors> {

  public static Constructors of(ImmutableSet<Constructor<?>> constructors) {
    return new Constructors(constructors);
  }

  private final ImmutableSet<Constructor<?>> constructors;

  private Constructors(ImmutableSet<Constructor<?>> constructors) {
    checkNotNull(constructors);
    this.constructors = constructors;
  }

  public Constructors annotatedWith(final Class<? extends Annotation> annotation) {
    return filter(predicateConstructorAnnotatedWith(annotation));
  }

  @Override
  public Constructors filter(Predicate<Constructor<?>> p) {
    return of(FluentIterable.from(constructors).filter(p).toSet());
  }

  @Override
  public ImmutableSet<Constructor<?>> get() {
    return constructors;
  }
}
