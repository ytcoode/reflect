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

import static io.ytcode.reflect.util.Utils.combineModifiers;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.Filterable;
import io.ytcode.reflect.util.Utils;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/** @author wangyuntao */
public abstract class AnnotatedElements<
        E extends AnnotatedElement, A extends AnnotatedElements<E, A>>
    extends Filterable<E, A> {

  protected AnnotatedElements(ImmutableSet<E> set) {
    super(set);
  }

  public A annotatedWith(final Class<? extends Annotation> annotation) {
    return filter(Utils.<E>predicateAnnotatedWith(annotation));
  }

  public final A modifiers(int... modifiers) {
    final int m = combineModifiers(modifiers);
    return filter(
        new Predicate<E>() {
          @Override
          public boolean apply(E input) {
            return (getModifier(input) & m) == m;
          }
        });
  }

  protected abstract int getModifier(E e);
}
