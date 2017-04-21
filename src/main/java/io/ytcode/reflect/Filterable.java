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

package io.ytcode.reflect;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.util.ReflectException;
import java.lang.reflect.Constructor;
import java.util.Iterator;

/** @author wangyuntao */
public abstract class Filterable<E, F extends Filterable<E, F>>
    implements Iterable<E>, Supplier<ImmutableSet<E>> {

  private final ImmutableSet<E> set;

  protected Filterable(ImmutableSet<E> set) {
    checkNotNull(set);
    this.set = set;
  }

  public final F filter(Predicate<E> p) {
    return with(FluentIterable.from(set).filter(p).toSet());
  }

  @SuppressWarnings("unchecked")
  public F with(ImmutableSet<E> set) {
    try {
      Constructor<F> c =
          (Constructor<F>) this.getClass().getDeclaredConstructor(ImmutableSet.class);
      c.setAccessible(true);
      return c.newInstance(set);
    } catch (Exception e) {
      throw new ReflectException(e);
    }
  }

  @Override
  public final Iterator<E> iterator() {
    return set.iterator();
  }

  @Override
  public final ImmutableSet<E> get() {
    return set;
  }

  public final int size() {
    return set.size();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass()).add("size", size()).toString();
  }
}
