package com.demo;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class SearchRegexTest {

    @DataPoints
    public static String[][] inputData() {
        int size = 6;
        String[][] data = new String[size][];
        data[0] = new String[] {"^make:.*model:.*year:2001$", new Search(null, "", 2001).getSearchRegex() };
        data[1] = new String[] {"^make:.*toyota.*model:.*year:2001$", new Search("toyota", "", 2001).getSearchRegex() };
        data[2] = new String[] {"^make:.*toyota.*model:.*year:2002$", new Search("TOYOTA", null, 2002).getSearchRegex() };
        data[3] = new String[] {"^make:.*model:.*camry.*year:2003$", new Search("", "Camry", 2003).getSearchRegex() };
        data[4] = new String[] {"^make:.*toyota.*model:.*corolla.*year:2000$", new Search("toYotA", "Corolla", 2000).getSearchRegex() };
        data[5] = new String[] {"^make:.*model:.*year:.*$", new Search("", "", -1).getSearchRegex() };
        return data;
    }

    @Theory
    public void testSearchRegex(String[] input) {
        assumeTrue(input != null);
        assertEquals(input[0], input[1]);
    }

}
