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
package com.github.stdi_sys.stindex.persist;

import java.util.function.Supplier;

public interface PersistableRegistrySpi {

  public PersistableIdAndConstructor[] getSupportedPersistables();

  public static class PersistableIdAndConstructor {
    private final short persistableId;
    private final Supplier<Persistable> persistableConstructor;

    public PersistableIdAndConstructor(
        final short persistableId,
        final Supplier<Persistable> persistableConstructor) {
      this.persistableId = persistableId;
      this.persistableConstructor = persistableConstructor;
    }

    public short getPersistableId() {
      return persistableId;
    }

    public Supplier<Persistable> getPersistableConstructor() {
      return persistableConstructor;
    }
  }
}
