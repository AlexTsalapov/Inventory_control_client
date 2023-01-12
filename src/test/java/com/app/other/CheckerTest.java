package com.app.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    @Test
    void checkDateManufacture() {
       assertEquals(Checker.checkDateManufacture("12.12.2021"),true);
    }

    @Test
    void checkDateExpiration() {
        assertEquals(Checker.checkDateExpiration("12.12.2023"),true);
    }
    @Test
    void checkDateManufacture1() {
        assertEquals(Checker.checkDateManufacture("12.12.2025"),false);
    }

    @Test
    void checkDateExpiration1() {
        assertEquals(Checker.checkDateExpiration("12.12.2021"),false);
    }
}