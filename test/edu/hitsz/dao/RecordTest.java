package edu.hitsz.dao;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @Test
    void getCurDate() {

        Date a=Record.getCurDate();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss").format(a));
    }
}