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

/** Convenience methods for comparing floating point values. */
public class FloatCompareUtils {
  public static final double COMP_EPSILON = 2.22E-16;

  /**
   * The == operator is not reliable for doubles, so we are using this method to check if two
   * doubles are equal
   *
   * @param x
   * @param y
   * @return true if the double are equal, false if they are not
   */
  public static boolean checkDoublesEqual(final double x, final double y) {
    return checkDoublesEqual(x, y, COMP_EPSILON);
  }

  /**
   * The == operator is not reliable for doubles, so we are using this method to check if two
   * doubles are equal
   *
   * @param x
   * @param y
   * @param epsilon
   * @return true if the double are equal, false if they are not
   */
  public static boolean checkDoublesEqual(final double x, final double y, final double epsilon) {
    boolean xNeg = false;
    boolean yNeg = false;
    final double diff = (Math.abs(x) - Math.abs(y));

    if (x < 0.0) {
      xNeg = true;
    }
    if (y < 0.0) {
      yNeg = true;
    }
    return ((diff <= epsilon) && (diff >= -epsilon) && (xNeg == yNeg));
  }
}
