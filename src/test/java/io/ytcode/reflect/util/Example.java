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

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.Invokable;
import io.ytcode.reflect.clazz.Classes;
import io.ytcode.reflect.clazz.Constructors;
import io.ytcode.reflect.clazz.Fields;
import io.ytcode.reflect.clazz.Methods;
import io.ytcode.reflect.resource.Resource;
import io.ytcode.reflect.resource.Resources;
import io.ytcode.reflect.resource.Scanner;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** @author wangyuntao */
public class Example {

  public static void main(String[] args) {
    System.out.println("### scanner ###");
    scanner();

    System.out.println("### resources ###");
    resources();

    System.out.println("### classes ###");
    classes();

    System.out.println("### fields ###");
    fields();

    System.out.println("### methods ###");
    methods();

    System.out.println("### constructors ###");
    constructors();
  }

  private static void scanner() {
    Resources rs1 = Scanner.pkgs("io.ytcode.reflect").scan();
    for (Resource r : rs1) {
      System.out.println(r);
    }

    Resources rs2 = Scanner.paths("/io/ytcode/reflect/").scan();
    System.out.println(rs2.size());

    Resources rs3 = Scanner.paths("/").scan();
    System.out.println(rs3.size());

    Resources rs4 =
        Scanner.from(
                ImmutableSet.of(
                    ClassLoader.getSystemClassLoader(),
                    ClassLoader.getSystemClassLoader().getParent()),
                ImmutableSet.of("/io/ytcode/reflect/clazz/", "/io/ytcode/reflect/resource/"),
                false)
            .scan();
    System.out.println(rs4.size());
  }

  private static void resources() {
    Resources rs1 = Scanner.paths("/io/ytcode/reflect/").scan();

    Resources rs2 = rs1.pattern(".*/resource/.*").suffix(".class");
    System.out.println(rs2.size());

    Resources rs3 =
        rs1.filter(
            new Predicate<Resource>() {
              @Override
              public boolean apply(Resource r) {
                return r.name().endsWith(".xml");
              }
            });
    System.out.println(rs3.size());
  }

  private static void classes() {
    Classes cs1 = Scanner.paths("/io/ytcode/reflect/").scan().classes();

    Classes cs2 = cs1.subTypeOf(Filterable.class);
    for (Class<?> c : cs2) {
      System.out.println(c);
    }

    Classes cs3 = cs1.annotatedWith(Beta.class).filter(Predicates.<Class<?>>equalTo(Classes.class));
    System.out.println(cs3.size());
  }

  private static void fields() {
    Fields fs1 = Scanner.paths("/io/ytcode/reflect/").scan().classes().fields();

    Fields fs2 =
        fs1.annotatedWith(Beta.class)
            .filter(
                new Predicate<Field>() {
                  @Override
                  public boolean apply(Field f) {
                    return Modifier.isStatic(f.getModifiers());
                  }
                });

    System.out.println(fs2);
  }

  private static void methods() {
    Methods ms1 = Scanner.paths("/io/ytcode/reflect/").scan().classes().methods();

    Methods ms2 =
        ms1.filter(
            new Predicate<Method>() {
              @Override
              public boolean apply(Method m) {
                return Invokable.from(m).isPublic();
              }
            });

    System.out.println(ms2);
  }

  private static void constructors() {
    Constructors cs1 = Scanner.paths("/io/ytcode/reflect/").scan().classes().constructors();

    Constructors cs2 =
        cs1.filter(
            new Predicate<Constructor<?>>() {
              @Override
              public boolean apply(Constructor<?> input) {
                return Invokable.from(input).isPublic();
              }
            });

    System.out.println(cs2);
  }
}
