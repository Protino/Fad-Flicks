/*
 *    Copyright 2016 Gurupad Mamadapur
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.calgen.prodek.fadflicks.utils;

import android.test.AndroidTestCase;

/**
 * Created by Gurupad Mamadapur on 11/4/2016.
 */

public class TestParser extends AndroidTestCase {

    public void testFormatReleaseDate() {
        String[] inputCases = new String[]{"2016-10-25", "2016-07-07", "2016-01-30", "1994-12-8"};
        String[] expectedOutputs = new String[]{"Oct 2016", "Jul 2016", "Jan 2016", "Dec 1994"};
        for (int i = 0; i < inputCases.length; i++) {
            assertEquals("Incorrect formatting of dates"
                    , expectedOutputs[i]
                    , Parser.formatReleaseDate(inputCases[i]));
        }
    }
}
