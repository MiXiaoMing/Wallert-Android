package com.qiumi.app.support;

import com.qiumi.app.support.utils.NullUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testNullUtil() {
        assertEquals(true, NullUtil.isNull(null));

        String s = null;
        assertEquals(true, NullUtil.isNull(s));
        s = "";
        assertEquals(false, NullUtil.isNull(s));
        assertEquals(true, NullUtil.isNotNull(s));

        assertEquals(false, NullUtil.isNull(1));
        assertEquals(true, NullUtil.isNotNull(1));

        Integer integer = null;
        assertEquals(true, NullUtil.isNull(integer));
        integer = 1;
        assertEquals(false, NullUtil.isNull(integer));
        assertEquals(true, NullUtil.isNotNull(integer));
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}