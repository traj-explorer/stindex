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

import java.util.List;

/**
 * @author Yu Liebing
 */
public class TemporalByteArray extends ByteArray {

  public TemporalByteArray() {
    this(EMPTY_BYTE_ARRAY);
  }

  public TemporalByteArray(final byte[] bytes) {
    this.bytes = bytes;
  }

  public TemporalByteArray(final String string) {
    bytes = StringUtils.stringToBinary(string);
    this.string = string;
  }

  public TemporalByteArray(final long value) {
    bytes = ByteArrayUtils.longToByteArray(value);
  }

  public TemporalByteArray(final List<Byte> bytes) {
    this.bytes = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      this.bytes[i] = bytes.get(i);
    }
  }

  @Override
  public String toString() {
    if (string != null) {
      return string;
    }
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02d", b));
    }
    string = sb.toString();
    return string;
  }
}
