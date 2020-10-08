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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SinglePartitionQueryRanges {
  private final byte[] partitionKey;

  private final Collection<ByteArrayRange> sortKeyRanges;

  public SinglePartitionQueryRanges(
      final byte[] partitionKey,
      final Collection<ByteArrayRange> sortKeyRanges) {
    this.partitionKey = partitionKey;
    this.sortKeyRanges = sortKeyRanges;
  }

  public SinglePartitionQueryRanges(final byte[] partitionKey) {
    this.partitionKey = partitionKey;
    sortKeyRanges = null;
  }

  public SinglePartitionQueryRanges(final List<ByteArrayRange> sortKeyRanges) {
    this.sortKeyRanges = sortKeyRanges;
    partitionKey = null;
  }

  public SinglePartitionQueryRanges(final ByteArrayRange singleSortKeyRange) {
    sortKeyRanges = Collections.singletonList(singleSortKeyRange);
    partitionKey = null;
  }

  public byte[] getPartitionKey() {
    return partitionKey;
  }

  public Collection<ByteArrayRange> getSortKeyRanges() {
    return sortKeyRanges;
  }

  public ByteArrayRange getSingleRange() {
    byte[] start = null;
    byte[] end = null;

    for (final ByteArrayRange range : sortKeyRanges) {
      if ((start == null) || (ByteArrayUtils.compare(range.getStart().getBytes(), start) < 0)) {
        start = range.getStart().getBytes();
      }
      if ((end == null) || (ByteArrayUtils.compare(range.getEnd().getBytes(), end) > 0)) {
        end = range.getEnd().getBytes();
      }
    }
    return new ByteArrayRange(start, end);
  }
}
