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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

/** @author wangyuntao */
public class PathFilter {

  public static PathFilter of(
      ImmutableSet<String> paths, boolean recursive, boolean keepLeadingSlash) {
    return new PathFilter(paths, recursive, keepLeadingSlash);
  }

  private final ImmutableSet<String> paths;
  private final boolean recursive;
  private final boolean keepLeadingSlash;

  private PathFilter(ImmutableSet<String> paths, boolean recursive, boolean keepLeadingSlash) {
    checkNotNull(paths);
    this.paths = normalizePaths(paths, keepLeadingSlash);
    this.recursive = recursive;
    this.keepLeadingSlash = keepLeadingSlash;
  }

  public boolean isValid(String path) {
    path = pathDir(path);
    if (!recursive) {
      return paths.contains(path);
    }

    for (String s : paths) {
      if (path.startsWith(s)) {
        return true;
      }
    }
    return false;
  }

  private String pathDir(String path) {
    int idx = path.lastIndexOf('/');
    if (idx < 0) {
      return keepLeadingSlash ? "/" : "";
    } else {
      return path.substring(0, idx + 1);
    }
  }

  private static ImmutableSet<String> normalizePaths(
      ImmutableSet<String> paths, final boolean keepLeadingSlash) {
    return FluentIterable.from(paths)
        .transform(
            new Function<String, String>() {
              @Override
              public String apply(String s) {
                s = CharMatcher.whitespace().trimFrom(s);
                s = CharMatcher.is('/').trimFrom(s);
                if (keepLeadingSlash) {
                  s = "/" + s;
                }
                return s + "/";
              }
            })
        .toSet();
  }
}
