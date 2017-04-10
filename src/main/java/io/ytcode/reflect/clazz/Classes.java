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
import static io.ytcode.reflect.util.Utils.*;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.util.Filter;
import java.lang.annotation.Annotation;

/** @author wangyuntao */
public class Classes implements Supplier<ImmutableSet<Class<?>>>, Filter<Class<?>, Classes> {

  public static Classes of(ImmutableSet<Class<?>> classes) {
    return new Classes(classes);
  }

  private final ImmutableSet<Class<?>> classes;

  private Classes(ImmutableSet<Class<?>> classes) {
    checkNotNull(classes);
    this.classes = classes;
  }

  public Classes subTypeOf(Class<?> cls) {
    return filter(predicateClassSubtypeOf(cls));
  }

  public Classes annotatedWith(Class<? extends Annotation> annotation) {
    return filter(predicateClassAnnotatedWith(annotation));
  }

  @Override
  public Classes filter(Predicate<Class<?>> p) {
    return of(FluentIterable.from(classes).filter(p).toSet());
  }

  @Override
  public ImmutableSet<Class<?>> get() {
    return classes;
  }

  public Fields fields() {
    return Fields.of(toFields(classes));
  }

  public Methods methods() {
    return Methods.of(toMethods(classes));
  }

  public Constructors constructors() {
    return Constructors.of(toConstructors(classes));
  }
}
