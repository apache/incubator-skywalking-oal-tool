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

import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class ScriptParserTest {
    @Test
    public void testParse() throws IOException {
        ScriptParser parser = ScriptParser.createFromScriptText(
            "Endpoint_avg = from(Endpoint.latency).avg(); //comment test" + "\n" +
                "Service_avg = from(Endpoint.latency).avg()"
        );
        List<AnalysisResult> results = parser.parse();

        Assert.assertEquals(2, results.size());

        AnalysisResult endpointAvg = results.get(0);
        Assert.assertEquals("Endpoint_avg", endpointAvg.getMetricName());

        AnalysisResult serviceAvg = results.get(1);
        Assert.assertEquals("Service_avg", serviceAvg.getMetricName());
    }
}
