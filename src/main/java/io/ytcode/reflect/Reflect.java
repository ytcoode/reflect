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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Java reflection utilities
 *
 * @author wangyuntao
 */
public class Reflect {

  public static ImmutableSet<Class<?>> superTypes(Class<?> c) {
    return superTypes(c, Predicates.<Class<?>>alwaysTrue());
  }

  public static ImmutableSet<Class<?>> superTypes(Class<?> c, Predicate<Class<?>> p) {
    checkNotNull(c);
    checkNotNull(p);

    ImmutableSet.Builder<Class<?>> b = ImmutableSet.builder();
    Class<?> c1 = c.getSuperclass();
    if (c1 != null) {
      if (p.apply(c1)) {
        b.add(c1);
      }
      b.addAll(superTypes(c1, p));
    }

    Class<?>[] c2s = c.getInterfaces();
    if (c2s != null) {
      for (Class<?> c2 : c2s) {
        if (p.apply(c2)) {
          b.add(c2);
        }
        b.addAll(superTypes(c2, p));
      }
    }

    return b.build();
  }

  public static ImmutableSet<Field> fields(Class<?> c) {
    return fields(c, Predicates.<Field>alwaysTrue());
  }

  public static ImmutableSet<Field> fields(Class<?> c, Predicate<Field> p) {
    checkNotNull(c);
    checkNotNull(p);

    ImmutableSet.Builder<Field> b = ImmutableSet.builder();
    for (Field field : c.getDeclaredFields()) {
      if (p.apply(field)) {
        b.add(field);
      }
    }

    for (Class<?> cls : superTypes(c)) {
      for (Field field : cls.getDeclaredFields()) {
        if (p.apply(field)) {
          b.add(field);
        }
      }
    }
    return b.build();
  }

  public static ImmutableSet<Method> methods(Class<?> c) {
    return methods(c, Predicates.<Method>alwaysTrue());
  }

  public static ImmutableSet<Method> methods(Class<?> c, Predicate<Method> p) {
    checkNotNull(c);
    checkNotNull(p);

    ImmutableSet.Builder<Method> b = ImmutableSet.builder();
    for (Method method : c.getDeclaredMethods()) {
      if (p.apply(method)) {
        b.add(method);
      }
    }

    for (Class<?> cls : superTypes(c)) {
      for (Method method : cls.getDeclaredMethods()) {
        if (p.apply(method)) {
          b.add(method);
        }
      }
    }
    return b.build();
  }

  public static ImmutableSet<Constructor<?>> constructors(Class<?> c) {
    return constructors(c, Predicates.<Constructor<?>>alwaysTrue());
  }

  public static ImmutableSet<Constructor<?>> constructors(Class<?> c, Predicate<Constructor<?>> p) {
    checkNotNull(c);
    checkNotNull(p);

    ImmutableSet.Builder<Constructor<?>> b = ImmutableSet.builder();
    for (Constructor<?> constructor : c.getDeclaredConstructors()) {
      if (p.apply(constructor)) {
        b.add(constructor);
      }
    }

    for (Class<?> cls : superTypes(c)) {
      for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
        if (p.apply(constructor)) {
          b.add(constructor);
        }
      }
    }
    return b.build();
  }
}
