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
package com.github.stdi_sys.stindex.spatial.sfc;

import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.hilbert.HilbertSFC;
import com.github.stdi_sys.stindex.spatial.sfc.zorder.ZOrderSFC;
import com.github.stdi_sys.stindex.spatial.sfc.xz.XZOrderSFC;

/**
 * Factory used to generate an instance of a known space filling curve type
 *
 * @author Yu Liebing
 * */
public class SFCFactory {
  /**
   * Generates a SFC instance based on the dimensions definition and the space filling curve type
   *
   * @param dimensionDefs specifies the min, max, and cardinality for this instance of the SFC
   * @param sfc specifies the type (Hilbert, ZOrder) of space filling curve to generate
   * @return a space filling curve instance generated based on the supplied parameters
   */
  public static GridIndex createSpaceFillingCurve(
      final SFCDimensionDefinition[] dimensionDefs,
      final SFCType sfc) {

    switch (sfc) {
      case HILBERT:
        return new HilbertSFC(dimensionDefs);

      case ZORDER:
        return new ZOrderSFC(dimensionDefs);

      case XZORDER:
        return new XZOrderSFC(dimensionDefs);
    }

    return null;
  }

  /**
   * Implemented and registered Space Filling curve types
   * */
  public static enum SFCType {
    HILBERT, ZORDER, XZORDER
  }
}
