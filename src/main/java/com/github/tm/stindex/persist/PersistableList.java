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
package com.github.tm.stindex.persist;

import java.nio.ByteBuffer;
import java.util.List;
import com.google.common.collect.Lists;

/**
 * A Persistable list of Persistables.
 */
public class PersistableList implements Persistable {

  private final List<Persistable> persistables;

  public PersistableList() {
    persistables = Lists.newArrayList();
  }

  public PersistableList(final List<Persistable> persistables) {
    this.persistables = persistables;
  }

  @Override
  public byte[] toBinary() {
    final List<byte[]> parts = Lists.newArrayListWithCapacity(persistables.size());
    int length = 4;
    for (final Persistable persistable : persistables) {
      final byte[] binary = PersistenceUtils.toBinary(persistable);
      length += binary.length + 4;
      parts.add(binary);
    }
    final ByteBuffer buffer = ByteBuffer.allocate(length);
    buffer.putInt(persistables.size());
    for (final byte[] part : parts) {
      buffer.putInt(part.length);
      buffer.put(part);
    }
    return buffer.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buffer = ByteBuffer.wrap(bytes);
    final int length = buffer.getInt();
    persistables.clear();
    for (int i = 0; i < length; i++) {
      final int partLength = buffer.getInt();
      final byte[] part = new byte[partLength];
      buffer.get(part);
      persistables.add(PersistenceUtils.fromBinary(part));
    }
  }

  public List<Persistable> getPersistables() {
    return persistables;
  }

}
