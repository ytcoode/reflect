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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** @author wangyuntao */
public class ScannerTest {

  @Test
  public void testScanPkgs() {
    Resources r = Scanner.pkgs("io.ytcode.reflect").scan();
    assertTrue(r.classes().get().contains(Scanner.class));
  }

  @Test
  public void testScanPaths() {
    Resources r = Scanner.paths("/io/ytcode/reflect").scan();
    assertTrue(r.classes().get().contains(Scanner.class));
  }

  @Test
  public void testScanRootPath() {
    assertTrue(Scanner.paths("/").scan().size() > 0);
  }
}
