package edu.hitsz.dao;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordDAOImplTest {

    @Test
    void doAdd() throws IOException {
        RecordDAOImpl data=new RecordDAOImpl();
        Record add=new Record(222);
        data.doAdd(add);
        add=new Record(444);
        data.doAdd(add);
        add=new Record(333);
        data.doAdd(add);
        add=new Record(555);
        data.doAdd(add);
        List<Record> recordList=data.getAllRecords();
        for(Record record:recordList){
            System.out.println(record);
        }

    }

    @Test
    void getAllRecords() throws IOException {
        RecordDAOImpl data=new RecordDAOImpl();
        Record add=new Record(222);
        data.doAdd(add);
        add=new Record(444);
        data.doAdd(add);
        add=new Record(333);
        data.doAdd(add);
        add=new Record(555);
        data.doAdd(add);

        data=new RecordDAOImpl();
        List<Record> recordList=data.getAllRecords();
        for(Record record:recordList){
            System.out.println(record);
        }
    }
}