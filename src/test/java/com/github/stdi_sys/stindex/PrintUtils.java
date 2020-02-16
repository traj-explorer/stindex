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
package com.github.stdi_sys.stindex;

import com.github.stdi_sys.stindex.spatial.GridIndex;

import java.util.BitSet;

/**
 * @author Yu Liebing
 */
public class PrintUtils {

  public static void printGridIndexValues(GridIndex gridIndex, int res) {
    double lngUnit = 360 / Math.pow(2, res);
    double latUnit = 180 / Math.pow(2, res);

    double startLng = -180.0 + lngUnit / 2;
    double startLat = 90.0 - latUnit / 2;

    double curLat = startLat;
    while (true) {
      if (curLat <= -90.0) break;
      double curLng = startLng;
      while (true) {
        if (curLng >= 180.0) break;
        System.out.print(gridIndex.getIndex(new double[]{curLng, curLat}).toLong() + " ");
        curLng += lngUnit;
      }
      System.out.println();
      curLat -= latUnit;
    }
  }

  public static void printGridIndexValues(GridIndex gridIndex, int lonRes, int latRes) {
    double lngUnit = 360 / Math.pow(2, lonRes);
    double latUnit = 180 / Math.pow(2, latRes);

    double startLng = -180.0 + lngUnit / 2;
    double startLat = 90.0 - latUnit / 2;

    double curLat = startLat;
    while (true) {
      if (curLat <= -90.0) break;
      double curLng = startLng;
      while (true) {
        if (curLng >= 180.0) break;
        System.out.print(gridIndex.getIndex(new double[]{curLng, curLat}).toLong() + " ");
        curLng += lngUnit;
      }
      System.out.println();
      curLat -= latUnit;
    }
  }

  public static void printBitset(BitSet bitSet) {
    System.out.print("{");
    for (int i = 0; i < bitSet.length(); i++) {
      if (bitSet.get(i)) {
        System.out.print("1 ");
      } else {
        System.out.print("0 ");
      }
    }
    System.out.println("}");
  }
}
