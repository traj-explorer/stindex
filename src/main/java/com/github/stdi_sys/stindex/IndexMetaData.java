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

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public interface IndexMetaData extends Mergeable {
  /**
   * Update the aggregation result using the new entry provided
   *
   * @param insertionIds the new indices to compute an updated aggregation result on
   */
  public void insertionIdsAdded(InsertionIds insertionIds);

  /**
   * Update the aggregation result by removing the entries provided
   *
   * @param insertionIds the new indices to compute an updated aggregation result on
   */
  public void insertionIdsRemoved(InsertionIds insertionIds);

  /** Create a JSON object that shows all the metadata handled by this object */
  public JSONObject toJSONObject() throws JSONException;
}
