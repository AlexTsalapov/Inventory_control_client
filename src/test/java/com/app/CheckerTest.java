package com.app;

import com.app.other.Checker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    @Test
    void checkDateManufacture() {
       boolean check= Checker.checkDateManufacture("12.12.2020");
       boolean score=true;
       assertEquals(score,check);
    }
    @Test
    void checkDateManufacture1() {
        boolean check= Checker.checkDateManufacture("12.12.2023");
        boolean score=false;
        assertEquals(score,check);
    }
    @Test
    void checkDateManufacture2() {
        boolean check= Checker.checkDateManufacture("12.13.2020");
        boolean score=false;
        assertEquals(score,check);
    }

    @Test
    void checkDateExpiration() {
        boolean check= Checker.checkDateExpiration("12.12.2020");
        boolean score=false;
        assertEquals(score,check);
    }
    @Test
    void checkDateExpiration1() {
        boolean check= Checker.checkDateExpiration("12.12.2022");
        boolean score=true;
        assertEquals(score,check);
    }
    @Test
    void checkDateExpiration2() {
        boolean check= Checker.checkDateExpiration("12.12.2080");
        boolean score=true;
        assertEquals(score,check);
    }


}