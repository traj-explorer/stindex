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

/**
 * A simple interface for persisting objects, PersistenceUtils provides convenience methods for
 * serializing and de-serializing these objects
 *
 * @author Yu Liebing
 */
public interface Persistable {
  /**
   * Convert fields and data within an object to binary form for transmission or storage.
   *
   * @return an array of bytes representing a binary stream representation of the object.
   */
  byte[] toBinary();

  /**
   * Convert a stream of binary bytes to fields and data within an object.
   * */
  void fromBinary(byte[] bytes);
}
