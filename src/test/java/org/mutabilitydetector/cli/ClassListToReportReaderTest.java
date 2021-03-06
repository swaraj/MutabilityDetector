package org.mutabilitydetector.cli;

/*
 * #%L
 * MutabilityDetector
 * %%
 * Copyright (C) 2008 - 2014 Graham Allan
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mutabilitydetector.locations.Dotted.dotted;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mutabilitydetector.locations.Dotted;

public class ClassListToReportReaderTest {

    private BufferedReader reader;
    private ClassListToReportCollector classListReader;

    @Before
    public void setUp() {
        reader = mock(BufferedReader.class);
    }

    @Test
    public void readsClassesFromPlainTextFile() throws Exception {
        when(reader.readLine()).thenReturn("java.lang.String", "java.io.FileReader", "org.junit.Test", null);
        classListReader = new PlainTextClassListToReportReader(reader);
        Collection<Dotted> classListToReport = classListReader.classListToReport();

        assertEquals("Should contain three classes.", 3, classListToReport.size());
        assertContainsClassName(classListToReport, "java.lang.String");
        assertContainsClassName(classListToReport, "java.io.FileReader");
        assertContainsClassName(classListToReport, "org.junit.Test");
    }

    private void assertContainsClassName(Collection<Dotted> classListToReport, String className) {
        assertTrue("Should contain the class [" + className + "].", classListToReport.contains(dotted(className)));
    }

    @Test(expected = ClassListException.class)
    public void classListExceptionIsThrownWhenReaderThrowsIOException() throws Exception {
        when(reader.readLine()).thenReturn("java.lang.String");
        when(reader.readLine()).thenThrow(new IOException());
        classListReader = new PlainTextClassListToReportReader(reader);

        classListReader.classListToReport();
    }

}
