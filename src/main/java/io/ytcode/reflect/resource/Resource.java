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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/** @author wangyuntao */
public class Resource {

  public static Resource of(String name, ClassLoader loader) {
    return new Resource(name, loader);
  }

  private final String name;
  private final ClassLoader loader;

  private Resource(String name, ClassLoader loader) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(loader);
    this.name = name;
    this.loader = loader;
  }

  public String name() {
    return name;
  }

  public ClassLoader loader() {
    return loader;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Resource) {
      Resource res = (Resource) obj;
      return name.equals(res.name) && loader == res.loader;
    }
    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(Resource.class)
        .add("name", name)
        .add("loader", loader)
        .toString();
  }
}
