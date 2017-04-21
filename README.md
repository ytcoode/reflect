# Reflect

***For making life better and easier.***

[![Build Status][travis-shield]][travis-link]
[![Maven Release][maven-shield]][maven-link]

## What is Reflect?

Reflect is a classpath scanner and filter, using it you can scan your classpath, filter your resources, classes, fields, methods and constructors.

It's ***immutable, fluent and simple***, so it's also thread safe and easy to use.

It's inspired by [Reflections][reflections] and [Guava ClassPath][guava-classpath].

## Usage

```java
// Scanner
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
```

```java
// Resources
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
```

```java
// Classes
Classes cs1 = Scanner.paths("/io/ytcode/reflect/").scan().classes();

Classes cs2 = cs1.subTypeOf(Filterable.class);
for (Class<?> c : cs2) {
  System.out.println(c);
}

Classes cs3 = cs1.annotatedWith(Beta.class).filter(Predicates.<Class<?>>equalTo(Classes.class));
System.out.println(cs3.size());

Classes cs4 = cs1.modifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
System.out.println(cs4.size());
```

```java
// Fields
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

Fields fs3 = fs1.modifiers(Modifier.PUBLIC, Modifier.STATIC);
System.out.println(fs3);
```

```java
// Methods
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

Methods ms3 = ms1.modifiers(Modifier.PUBLIC, Modifier.STATIC);
System.out.println(ms3);
```

```java
// Constructors
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

Constructors cs3 = cs1.modifiers(Modifier.PUBLIC);
System.out.println(cs3);
```

## License

Reflect is licensed under the open-source [Apache 2.0 license](LICENSE).

## Contributing

Please [see the guidelines for contributing](CONTRIBUTING.md) before creating pull requests.

<!-- references -->

[travis-shield]: https://img.shields.io/travis/wangyuntao/reflect.png
[travis-link]: https://travis-ci.org/wangyuntao/reflect
[maven-shield]: https://img.shields.io/maven-central/v/io.ytcode/reflect.png
[maven-link]: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.ytcode%22%20AND%20a%3A%22reflect%22
[guava-classpath]: https://github.com/google/guava/blob/master/guava/src/com/google/common/reflect/ClassPath.java
[reflections]: https://github.com/ronmamo/reflections