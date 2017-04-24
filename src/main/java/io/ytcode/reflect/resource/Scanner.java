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
import static io.ytcode.reflect.Reflect.pkgToResPath;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.ytcode.reflect.util.PathFilter;
import io.ytcode.reflect.util.ReflectException;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>Warning:</b> Currently only {@link URLClassLoader} and only {@code file://} urls are
 * supported.
 *
 * @author wangyuntao
 */
public class Scanner {

  private static final Logger logger = LoggerFactory.getLogger(Scanner.class);

  private static final Splitter CLASS_PATH_ATTRIBUTE_SPLITTER =
      Splitter.on(" ").trimResults().omitEmptyStrings();

  public static Scanner pkgs(String... pkgs) {
    return paths(pkgToResPath(pkgs));
  }

  public static Scanner paths(String... paths) {
    return from(
        ImmutableSet.of(Thread.currentThread().getContextClassLoader()),
        ImmutableSet.copyOf(paths),
        true);
  }

  public static Scanner from(
      ImmutableSet<ClassLoader> classLoaders, ImmutableSet<String> paths, boolean recursive) {
    return new Scanner(classLoaders, paths, recursive);
  }

  private final ImmutableSet<ClassLoader> classLoaders;
  private final PathFilter pathFilter;

  private Scanner(
      ImmutableSet<ClassLoader> classLoaders, ImmutableSet<String> paths, boolean recursive) {
    checkNotNull(classLoaders);
    this.classLoaders = classLoaders;
    this.pathFilter = PathFilter.of(paths, recursive, false);
  }

  public Resources scan() {
    return Resources.of(new ScanJob().scan());
  }

  private class ScanJob {
    private final ImmutableSet.Builder<Resource> b;
    private final Set<File> scannedFiles;

    ScanJob() {
      this.b = ImmutableSet.builder();
      this.scannedFiles = Sets.newHashSet();
    }

    ImmutableSet<Resource> scan() {
      for (ClassLoader classLoader : classLoaders) {
        if (!(classLoader instanceof URLClassLoader)) {
          logger.error(
              "Illegal class loader, currently only URLClassLoader is supported! {}", classLoader);
          continue;
        }

        URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
        for (URL url : urlClassLoader.getURLs()) {
          try {
            scan(url, classLoader);
          } catch (Exception e) {
            throw new ReflectException(e);
          }
        }
      }
      return b.build();
    }

    private void scan(URL url, ClassLoader loader) throws Exception {
      if (!url.getProtocol().equals("file")) {
        logger.error("Illegal url, currently only file:// url is supported! {}", url);
        return;
      }

      File file = new File(url.toURI());
      file = file.getCanonicalFile();

      if (!scannedFiles.add(file)) {
        return;
      }

      if (!file.exists()) {
        return;
      }

      if (file.isDirectory()) {
        scanDir(file, loader);
      } else {
        scanJar(file, loader);
      }
    }

    private void scanDir(File dir, ClassLoader loader) {
      scanDir(dir, loader, "");
    }

    private void scanDir(File dir, ClassLoader loader, String path) {
      File[] files = dir.listFiles();
      if (files == null) {
        return;
      }
      for (File file : files) {
        String pathName = path + file.getName();
        if (file.isDirectory()) {
          scanDir(file, loader, pathName + "/");
        } else {
          if (pathFilter.isValid(pathName)) {
            b.add(Resource.of(pathName, loader));
          }
        }
      }
    }

    private void scanJar(File file, ClassLoader loader) throws Exception {
      try (JarFile jarFile = new JarFile(file)) {
        scanJar(jarFile, loader);
        scanJarManifest(file, jarFile.getManifest(), loader);
      }
    }

    private void scanJar(JarFile jarFile, ClassLoader loader) {
      Enumeration<JarEntry> e = jarFile.entries();
      while (e.hasMoreElements()) {
        JarEntry entry = e.nextElement();
        if (!entry.isDirectory() && pathFilter.isValid(entry.getName())) {
          b.add(Resource.of(entry.getName(), loader));
        }
      }
    }

    private void scanJarManifest(File file, Manifest manifest, ClassLoader loader)
        throws Exception {
      if (manifest == null) {
        return;
      }

      String classPath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
      if (Strings.isNullOrEmpty(classPath)) {
        return;
      }

      for (String path : CLASS_PATH_ATTRIBUTE_SPLITTER.split(classPath)) {
        URL url = new URL(file.toURI().toURL(), path);
        scan(url, loader);
      }
    }
  }
}
