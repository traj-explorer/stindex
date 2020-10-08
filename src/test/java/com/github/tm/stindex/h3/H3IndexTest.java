/*
 * Copyright 2020 Yu Liebing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
package com.github.tm.stindex.h3;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.GeoCoord;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class H3IndexTest {

  H3Core h3Core = H3Core.newInstance();

  public H3IndexTest() throws IOException {
  }

  @Test
  public void getChildren() {
    long a = h3Core.geoToH3(40, 40, 6);
    List<Long> c = h3Core.h3ToChildren(a, 7);
    for (long v : c) {
      System.out.println(v);
    }

    GeoCoord upperLeft = new GeoCoord(30.45, 114.50);
    GeoCoord upperRight = new GeoCoord(30.45, 114.55);
    GeoCoord bottomRight = new GeoCoord(30.40, 114.55);
    GeoCoord bottomLeft = new GeoCoord(30.40, 114.50);
    List<GeoCoord> points = new ArrayList<>();
    points.add(upperLeft);
    points.add(upperRight);
    points.add(bottomRight);
    points.add(bottomLeft);
    points.add(upperLeft);
    List<Long> res = h3Core.polyfill(points, null, 6);
    System.out.println();
    for (long v : res) {
      System.out.println(v);
    }
  }
}