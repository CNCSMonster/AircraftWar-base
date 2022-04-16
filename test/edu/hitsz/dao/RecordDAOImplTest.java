package edu.hitsz.dao;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordDAOImplTest {


    @Test
    void testDoAdd() throws IOException {
//        RecordDAOImpl data=new RecordDAOImpl();
//        for(int i=0;i<30;i++){
//            Record add=new Record(i*17);
//            data.doAdd(add);
//        }
//
//        data=new RecordDAOImpl();
//
//        List<Record> recordList=data.getAllRecords();
//        System.out.println("打印记录列表中所有");
//        for(Record record:recordList){
//            System.out.println(record);
//        }

    }

    @Test
    void testGetAllRecords() throws IOException {
        RecordDAOImpl data=new RecordDAOImpl();
        List<Record> recordList=data.getAllRecords();
        for(Record record:recordList){
            System.out.println(record);
        }
    }
}