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

import static io.ytcode.reflect.util.Utils.*;

import com.google.common.collect.ImmutableSet;
import java.lang.annotation.Annotation;

/** @author wangyuntao */
public class Classes extends AnnotatedElements<Class<?>, Classes> {

  public static Classes of(ImmutableSet<Class<?>> classes) {
    return new Classes(classes);
  }

  private Classes(ImmutableSet<Class<?>> classes) {
    super(classes);
  }

  public Classes subTypeOf(Class<?> cls) {
    return filter(predicateClassSubTypeOf(cls));
  }

  public Classes annotatedWith(final Class<? extends Annotation> annotation) {
    return filter(predicateClassAnnotatedWith(annotation));
  }

  @Override
  protected int getModifier(Class<?> c) {
    return c.getModifiers();
  }

  public Fields fields() {
    return Fields.of(toFields(this));
  }

  public Methods methods() {
    return Methods.of(toMethods(this));
  }

  public Constructors constructors() {
    return Constructors.of(toConstructors(this));
  }
}
