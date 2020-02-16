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

/**
 * @author Yu Liebing
 */
public class STByteArray extends ByteArray {
  private int frontLen;
  private int backLen;

  public STByteArray(final ByteArray timeIndex, final ByteArray spatialIndex) {
    frontLen = timeIndex.length();
    backLen = 0;
    bytes = timeIndex.combine(spatialIndex).getBytes();
  }

  public STByteArray(final ByteArray frontTimeIndex,
                     final ByteArray spatialIndex,
                     final ByteArray backTimeIndex) {
    frontLen = frontTimeIndex.length();
    backLen = backTimeIndex.length();
    bytes = frontTimeIndex.combine(spatialIndex).combine(backTimeIndex).getBytes();
  }

  @Override
  public String toString() {
    if (string != null) {
      return string;
    }
    StringBuilder sb =  new StringBuilder();
    for (int i = 0; i < frontLen; i++) {
      sb.append(String.format("%02d", bytes[i]));
    }
    int spatialLen = bytes.length - frontLen - backLen;
    byte[] spatialBytes = new byte[spatialLen];
    System.arraycopy(bytes, frontLen, spatialBytes, 0, spatialLen);
    ByteArray spatialIndex = new SpatialByteArray(spatialBytes);
    sb.append(spatialIndex.toString());
    for (int i = frontLen + spatialLen; i < bytes.length; i++) {
      sb.append(String.format("%02d", bytes[i]));
    }
    string = sb.toString();
    return string;
  }
}
