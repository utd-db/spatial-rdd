/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package edu.utdallas.cg.spatial_rdd.file.parser;

import edu.utdallas.cg.spatial_rdd.enums.FileDataSplitter;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FormatMapper<T extends Geometry> extends FormatUtils
    implements FlatMapFunction<Iterator<String>, T> {

  /**
   * Instantiates a new format mapper. This is extensively used in SedonaSQL.
   *
   * @param splitter
   * @param carryInputData
   */
  public FormatMapper(FileDataSplitter splitter, boolean carryInputData) {
    super(splitter, carryInputData);
  }

  @Override
  public Iterator<T> call(Iterator<String> stringIterator) throws Exception {
    List<T> result = new ArrayList<>();
    while (stringIterator.hasNext()) {
      String line = stringIterator.next();
      if(line.contains("\"geometry\"")) addGeometry(readGeometry(line), result);
    }
    return result.iterator();
  }
}
