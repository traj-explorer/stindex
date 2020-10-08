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

import java.util.Arrays;

/**
 * This interface defines a multi-tiered approach to indexing, in which a single strategy is reliant
 * on a set of sub-strategies
 */
public interface HierarchicalNumericIndexStrategy extends NumericIndexStrategy {
  public SubStrategy[] getSubStrategies();

  public static class SubStrategy {
    private final NumericIndexStrategy indexStrategy;
    private final byte[] prefix;

    public SubStrategy(final NumericIndexStrategy indexStrategy, final byte[] prefix) {
      this.indexStrategy = indexStrategy;
      this.prefix = prefix;
    }

    public NumericIndexStrategy getIndexStrategy() {
      return indexStrategy;
    }

    public byte[] getPrefix() {
      return prefix;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((indexStrategy == null) ? 0 : indexStrategy.hashCode());
      result = (prime * result) + Arrays.hashCode(prefix);
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
      final SubStrategy other = (SubStrategy) obj;
      if (indexStrategy == null) {
        if (other.indexStrategy != null) {
          return false;
        }
      } else if (!indexStrategy.equals(other.indexStrategy)) {
        return false;
      }
      if (!Arrays.equals(prefix, other.prefix)) {
        return false;
      }
      return true;
    }
  }
}
