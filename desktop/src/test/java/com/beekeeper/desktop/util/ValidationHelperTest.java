package com.beekeeper.desktop.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationHelperTest {

    @Test
    void testIsValidDoubleWithValidInput() {
        assertTrue(ValidationHelper.isValidDouble("123.45"));
        assertTrue(ValidationHelper.isValidDouble("0"));
        assertTrue(ValidationHelper.isValidDouble("-10.5"));
        assertTrue(ValidationHelper.isValidDouble("0.001"));
    }

    @Test
    void testIsValidDoubleWithInvalidInput() {
        assertFalse(ValidationHelper.isValidDouble("abc"));
        assertFalse(ValidationHelper.isValidDouble("12.34.56"));
        assertFalse(ValidationHelper.isValidDouble("12a"));
    }

    @Test
    void testIsValidDoubleWithEmptyInput() {
        assertTrue(ValidationHelper.isValidDouble(""), "Empty string should be valid");
        assertTrue(ValidationHelper.isValidDouble("   "), "Whitespace should be valid");
        assertTrue(ValidationHelper.isValidDouble(null), "Null should be valid");
    }

    @Test
    void testIsValidIntegerWithValidInput() {
        assertTrue(ValidationHelper.isValidInteger("123"));
        assertTrue(ValidationHelper.isValidInteger("0"));
        assertTrue(ValidationHelper.isValidInteger("-10"));
    }

    @Test
    void testIsValidIntegerWithInvalidInput() {
        assertFalse(ValidationHelper.isValidInteger("abc"));
        assertFalse(ValidationHelper.isValidInteger("12.34"));
        assertFalse(ValidationHelper.isValidInteger("12a"));
    }

    @Test
    void testIsValidIntegerWithEmptyInput() {
        assertTrue(ValidationHelper.isValidInteger(""), "Empty string should be valid");
        assertTrue(ValidationHelper.isValidInteger("   "), "Whitespace should be valid");
        assertTrue(ValidationHelper.isValidInteger(null), "Null should be valid");
    }

    @Test
    void testIsInRange() {
        assertTrue(ValidationHelper.isInRange(5, 0, 10));
        assertTrue(ValidationHelper.isInRange(0, 0, 10));
        assertTrue(ValidationHelper.isInRange(10, 0, 10));
        assertFalse(ValidationHelper.isInRange(-1, 0, 10));
        assertFalse(ValidationHelper.isInRange(11, 0, 10));
    }

    @Test
    void testParseDouble() {
        assertEquals(123.45, ValidationHelper.parseDouble("123.45"), 0.001);
        assertEquals(0.0, ValidationHelper.parseDouble("0"), 0.001);
        assertEquals(-10.5, ValidationHelper.parseDouble("-10.5"), 0.001);
        assertEquals(0.0, ValidationHelper.parseDouble(""), 0.001);
        assertEquals(0.0, ValidationHelper.parseDouble(null), 0.001);
        assertEquals(0.0, ValidationHelper.parseDouble("abc"), 0.001);
    }

    @Test
    void testParseInt() {
        assertEquals(123, ValidationHelper.parseInt("123"));
        assertEquals(0, ValidationHelper.parseInt("0"));
        assertEquals(-10, ValidationHelper.parseInt("-10"));
        assertEquals(0, ValidationHelper.parseInt(""));
        assertEquals(0, ValidationHelper.parseInt(null));
        assertEquals(0, ValidationHelper.parseInt("abc"));
    }
}
