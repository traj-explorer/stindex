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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Defines a unit interval on a number line.
 *
 * @author Yu Liebing
 * */
public class ByteArrayRange implements Comparable<ByteArrayRange> {
  public static final ByteArrayRange EMPTY_BYTE_ARRAY_RANGE =
          new ByteArrayRange(ByteArray.EMPTY_BYTE_ARRAY, ByteArray.EMPTY_BYTE_ARRAY);

  protected ByteArray start;
  protected ByteArray end;
  protected boolean singleValue;

  public ByteArrayRange(final byte[] start, final byte[] end) {
    this(start, end, false);
  }

  public ByteArrayRange(final byte[] start, final byte[] end, final boolean singleValue) {
    this.start = new ByteArray(start);
    this.end = new ByteArray(end);
    this.singleValue = singleValue;
  }

  public ByteArrayRange(final ByteArray start, final ByteArray end) {
    this(start, end, false);
  }

  public ByteArrayRange(final ByteArray start, final ByteArray end, final boolean singleValue) {
    this.start = start;
    this.end = end;
    this.singleValue = singleValue;
  }

  public ByteArrayRange(final List<Byte> start, final List<Byte> end, boolean singleValue) {
    this.start = new ByteArray(start);
    this.end = new ByteArray(end);
    this.singleValue = singleValue;
  }

  public ByteArray getStart() {
    return start;
  }

  public ByteArray getEnd() {
    return end;
  }

  public byte[] getEndAsNextPrefix() {
    return ByteArrayUtils.getNextPrefix(end.getBytes());
  }

  public boolean isSingleValue() {
    return singleValue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((end == null) ? 0 : Arrays.hashCode(end.getBytes()));
    result = (prime * result) + (singleValue ? 1231 : 1237);
    result = (prime * result) + ((start == null) ? 0 : Arrays.hashCode(start.getBytes()));
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ByteArrayRange other = (ByteArrayRange) obj;
    if (end == null) {
      if (other.end != null) {
        return false;
      }
    } else if (!Arrays.equals(end.getBytes(), other.end.getBytes())) {
      return false;
    }
    if (singleValue != other.singleValue) {
      return false;
    }
    if (start == null) {
      if (other.start != null) {
        return false;
      }
    } else if (!Arrays.equals(start.getBytes(), other.start.getBytes())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    if (singleValue) {
      return start.toString();
    }
    return "[" + start.toString() + ", " + end.toString() + "]";
  }

  public boolean intersects(final ByteArrayRange other) {
    if (isSingleValue()) {
      if (other.isSingleValue()) {
        return Arrays.equals(start.getBytes(), other.start.getBytes());
      }
      return false;
    }
    return ((ByteArrayUtils.compare(start.getBytes(), other.getEndAsNextPrefix()) <= 0)
        && (ByteArrayUtils.compare(getEndAsNextPrefix(), other.start.getBytes()) >= 0));
  }

  public ByteArrayRange intersection(final ByteArrayRange other) {
    return new ByteArrayRange(
        ByteArrayUtils.compare(start.getBytes(), other.start.getBytes()) <= 0 ? other.start : start,
        ByteArrayUtils.compare(getEndAsNextPrefix(), other.getEndAsNextPrefix()) >= 0 ? other.end
            : end);
  }

  public boolean contains(final ByteArrayRange other) {
    if (isSingleValue()) {
      if (other.isSingleValue()) {
        return Arrays.equals(start.getBytes(), other.start.getBytes());
      }
      return false;
    }
    return ((ByteArrayUtils.compare(start.getBytes(), other.start.getBytes()) <= 0)
            && (ByteArrayUtils.compare(end.getBytes(), other.end.getBytes()) >= 0));
  }

  public ByteArrayRange union(final ByteArrayRange other) {
    return new ByteArrayRange(
        ByteArrayUtils.compare(start.getBytes(), other.start.getBytes()) <= 0 ? start : other.start,
        ByteArrayUtils.compare(getEndAsNextPrefix(), other.getEndAsNextPrefix()) >= 0 ? end
            : other.end);
  }

  @Override
  public int compareTo(final ByteArrayRange other) {
    final int diff = ByteArrayUtils.compare(start.getBytes(), other.start.getBytes());
    return diff != 0 ? diff
        : ByteArrayUtils.compare(getEndAsNextPrefix(), other.getEndAsNextPrefix());
  }

  public enum MergeOperation {
    UNION, INTERSECTION
  }

  public static final Collection<ByteArrayRange> mergeIntersections(
      final Collection<ByteArrayRange> ranges,
      final MergeOperation op) {
    final List<ByteArrayRange> rangeList = new ArrayList<>(ranges);
    // sort order so the first range can consume following ranges
    Collections.sort(rangeList);
    final List<ByteArrayRange> result = new ArrayList<>();
    for (int i = 0; i < rangeList.size();) {
      ByteArrayRange r1 = rangeList.get(i);
      int j = i + 1;
      for (; j < rangeList.size(); j++) {
        final ByteArrayRange r2 = rangeList.get(j);
        if (r1.intersects(r2)) {
          if (op.equals(MergeOperation.UNION)) {
            r1 = r1.union(r2);
          } else {
            r1 = r1.intersection(r2);
          }
        } else {
          break;
        }
      }
      i = j;
      result.add(r1);
    }
    return result;
  }
}
