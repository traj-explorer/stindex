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
package com.github.tm.stindex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convenience methods for converting to and from strings. The encoding and decoding of strings uses
 * UTF-8, and these methods should be used for serializing and deserializing text-based data, not
 * for converting binary data to a String representation. Use ByteArrayUtils for converting data
 * that is binary in nature to a String for transport.
 *
 * @author Yu Liebing
 */
public class StringUtils {
  public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  private static final String DEFAULT_GEOWAVE_CHARSET = "ISO-8859-1";
  public static final String GEOWAVE_CHARSET_PROPERTY_NAME = "geowave.charset";
  private static Charset geowaveCharset = null;

  public static Charset getGeoWaveCharset() {
    if (geowaveCharset == null) {
      final String charset =
          System.getProperty(GEOWAVE_CHARSET_PROPERTY_NAME, DEFAULT_GEOWAVE_CHARSET);
      geowaveCharset = Charset.forName(charset);
    }
    return geowaveCharset;
  }

  /**
   * Utility to convert a String to bytes
   *
   * @param string incoming String to convert
   * @return a byte array
   */
  public static byte[] stringToBinary(final String string) {
    return string.getBytes(getGeoWaveCharset());
  }

  /**
   * Utility to convert a String to bytes
   *
   * @param strings incoming String to convert
   * @return a byte array
   */
  public static byte[] stringsToBinary(final String strings[]) {
    int len = VarintUtils.unsignedIntByteLength(strings.length);
    final List<byte[]> strsBytes = new ArrayList<>();
    for (final String str : strings) {
      final byte[] strByte = str.getBytes(getGeoWaveCharset());
      strsBytes.add(strByte);
      len += (strByte.length + VarintUtils.unsignedIntByteLength(strByte.length));
    }
    final ByteBuffer buf = ByteBuffer.allocate(len);
    VarintUtils.writeUnsignedInt(strings.length, buf);
    for (final byte[] str : strsBytes) {
      VarintUtils.writeUnsignedInt(str.length, buf);
      buf.put(str);
    }
    return buf.array();
  }

  /**
   * Utility to convert bytes to a String
   *
   * @param binary a byte array to convert to a String
   * @return a String representation of the byte array
   */
  public static String stringFromBinary(final byte[] binary) {
    return new String(binary, getGeoWaveCharset());
  }

  /**
   * Utility to convert bytes to a String
   *
   * @param binary a byte array to convert to a String
   * @return a String representation of the byte array
   */
  public static String[] stringsFromBinary(final byte[] binary) {
    final ByteBuffer buf = ByteBuffer.wrap(binary);
    final int count = VarintUtils.readUnsignedInt(buf);
    final String[] result = new String[count];
    for (int i = 0; i < count; i++) {
      final int size = VarintUtils.readUnsignedInt(buf);
      final byte[] strBytes = ByteArrayUtils.safeRead(buf, size);
      result[i] = new String(strBytes, getGeoWaveCharset());
    }
    return result;
  }

  /**
   * Convert a number to a string. In this case we ensure that it is safe for Accumulo table names
   * by replacing '-' with '_'
   *
   * @param number the number to convert
   * @return the safe string representing that number
   */
  public static String intToString(final int number) {
    return org.apache.commons.lang3.StringUtils.replace(Integer.toString(number), "-", "_");
  }

  public static Map<String, String> parseParams(final String params) throws NullPointerException {
    final Map<String, String> paramsMap = new HashMap<>();
    final String[] paramsSplit = params.split(";");
    for (final String param : paramsSplit) {
      final String[] keyValue = param.split("=");
      if (keyValue.length != 2) {
        continue;
      }
      paramsMap.put(keyValue[0].trim(), keyValue[1].trim());
    }
    return paramsMap;
  }
}
