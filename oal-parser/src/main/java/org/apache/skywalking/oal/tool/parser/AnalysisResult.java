/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oal.tool.parser;

import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.remote.selector.Selector;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AnalysisResult {
    private String metricName;

    private String tableName;

    private String packageName;

    private String sourceName;

    private String sourceAttribute;

    private String aggregationFunctionName;

    private String indicatorClassName;

    private Selector remoteSelector;

    private boolean needMerge;

    private EntryMethod entryMethod;

    private List<DataColumn> persistentFields;

    private List<SourceColumn> fieldsFromSource;

    private PersistenceColumns serializeFields;

    public void addPersistentField(String fieldName, String columnName, Class<?> type) {
        if (persistentFields == null) {
            persistentFields = new LinkedList<>();
        }
        DataColumn dataColumn = new DataColumn(fieldName, columnName, type);
        persistentFields.add(dataColumn);
    }

    public void generateSerializeFields() {
        serializeFields = new PersistenceColumns();
        serializeFields.addLongField("timeBucket");
        for (SourceColumn sourceColumn : fieldsFromSource) {
            String type = sourceColumn.getType().getSimpleName();
            switch (type) {
                case "int":
                    serializeFields.addIntField(sourceColumn.getFieldName());
                    break;
                case "double":
                    serializeFields.addDoubleField(sourceColumn.getFieldName());
                    break;
                case "String":
                    serializeFields.addStringField(sourceColumn.getFieldName());
                    break;
                case "long":
                    serializeFields.addLongField(sourceColumn.getFieldName());
                    break;
                default:
                    throw new IllegalStateException("Unexpected field type [" + type + "] of source sourceColumn [" + sourceColumn.getFieldName() + "]");
            }
        }

        for (DataColumn column : persistentFields) {
            String type = column.getType().getSimpleName();
            switch (type) {
                case "int":
                    serializeFields.addIntField(column.getFieldName());
                    break;
                case "double":
                    serializeFields.addDoubleField(column.getFieldName());
                    break;
                case "String":
                    serializeFields.addStringField(column.getFieldName());
                    break;
                case "long":
                    serializeFields.addLongField(column.getFieldName());
                    break;
                default:
                    throw new IllegalStateException("Unexpected field type [" + type + "] of persistence column [" + column.getFieldName() + "]");
            }
        }
    }
}