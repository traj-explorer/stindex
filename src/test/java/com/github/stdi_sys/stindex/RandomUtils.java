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

import java.util.Random;

/**
 * @author Yu Liebing
 */
public class RandomUtils {

  private static Random random = new Random();

  private static double randomDouble(double start, double end) {
    int sign;
    if (random.nextDouble() <= 0.5) {
      sign = 1;
    } else {
      sign = -1;
    }
    return sign * (random.nextDouble() * (end - start) + start);
  }

  public static double randomLat() {
    return randomDouble(-90.0, 90.0);
  }

  public static double randomLon() {
    return randomDouble(-180.0, 180.0);
  }

  public static void main(String[] args) {
    System.out.println(randomLat());
    System.out.println(randomLon());
  }
}
