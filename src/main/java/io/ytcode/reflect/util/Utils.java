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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import io.ytcode.reflect.Reflect;
import io.ytcode.reflect.resource.Resource;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** @author wangyuntao */
public class Utils {

  public static Predicate<Resource> predicateResourceNameSuffix(final String suffix) {
    return Predicates.compose(
        new Predicate<String>() {
          @Override
          public boolean apply(String s) {
            return s.endsWith(suffix);
          }
        },
        new Function<Resource, String>() {
          @Override
          public String apply(Resource r) {
            return r.name();
          }
        });
  }

  public static Predicate<Resource> predicateResourceNamePattern(String pattern) {
    return Predicates.compose(
        Predicates.containsPattern(pattern),
        new Function<Resource, String>() {
          @Override
          public String apply(Resource r) {
            return r.name();
          }
        });
  }

  public static Predicate<Class<?>> predicateClassSubTypeOf(final Class<?> cls) {
    return new Predicate<Class<?>>() {
      @Override
      public boolean apply(Class<?> input) {
        return input != cls && cls.isAssignableFrom(input);
      }
    };
  }

  public static Predicate<Class<?>> predicateClassAnnotatedWith(
      final Class<? extends Annotation> annotation) {
    if (!annotation.isAnnotationPresent(Inherited.class)) {
      return predicateAnnotatedWith(annotation);
    }

    return new Predicate<Class<?>>() {
      @Override
      public boolean apply(Class<?> c) {
        while (c != null) {
          if (c.isAnnotationPresent(annotation)) {
            return true;
          }
          c = c.getSuperclass();
        }
        return false;
      }
    };
  }

  public static Predicate<Field> predicateFieldAnnotatedWith(
      final Class<? extends Annotation> annotation) {
    return predicateAnnotatedWith(annotation);
  }

  public static Predicate<Method> predicateMethodAnnotatedWith(
      final Class<? extends Annotation> annotation) {
    return predicateAnnotatedWith(annotation);
  }

  public static Predicate<Constructor<?>> predicateConstructorAnnotatedWith(
      final Class<? extends Annotation> annotation) {
    return predicateAnnotatedWith(annotation);
  }

  public static <T extends AnnotatedElement> Predicate<T> predicateAnnotatedWith(
      final Class<? extends Annotation> annotation) {
    return new Predicate<T>() {
      @Override
      public boolean apply(T input) {
        return input.isAnnotationPresent(annotation);
      }
    };
  }

  public static ImmutableSet<Class<?>> toClasses(Iterable<Resource> resources) {
    return FluentIterable.from(resources)
        .transform(
            new Function<Resource, Class<?>>() {
              @Override
              public Class<?> apply(Resource r) {
                String s = r.name();
                s = s.substring(0, s.length() - ".class".length());
                s = s.replace('/', '.');
                try {
                  return r.loader().loadClass(s);
                } catch (ClassNotFoundException e) {
                  throw new ReflectException(e);
                }
              }
            })
        .toSet();
  }

  public static ImmutableSet<Field> toFields(Iterable<Class<?>> classes) {
    return FluentIterable.from(classes)
        .transformAndConcat(
            new Function<Class<?>, ImmutableSet<Field>>() {
              @Override
              public ImmutableSet<Field> apply(Class<?> c) {
                return Reflect.fields(c);
              }
            })
        .toSet();
  }

  public static ImmutableSet<Method> toMethods(Iterable<Class<?>> classes) {
    return FluentIterable.from(classes)
        .transformAndConcat(
            new Function<Class<?>, ImmutableSet<Method>>() {
              @Override
              public ImmutableSet<Method> apply(Class<?> c) {
                return Reflect.methods(c);
              }
            })
        .toSet();
  }

  public static ImmutableSet<Constructor<?>> toConstructors(Iterable<Class<?>> classes) {
    return FluentIterable.from(classes)
        .transformAndConcat(
            new Function<Class<?>, ImmutableSet<Constructor<?>>>() {
              @Override
              public ImmutableSet<Constructor<?>> apply(Class<?> c) {
                return Reflect.constructors(c);
              }
            })
        .toSet();
  }
}
