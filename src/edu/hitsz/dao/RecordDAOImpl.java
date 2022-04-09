package edu.hitsz.dao;

import java.util.List;

public class RecordDAOImpl implements RecordDAO{
    //实现接口的类
    public static List<Record> list;

    static{
        //静态赋值，仅仅在类加载时作用一次
        list=getList();
    }

    private static List<Record> getList(){
        return null;
    }


    @Override
    public void doAdd(Record record) {
        list.add(record);
        //写入文件

    }

    @Override
    public List<Record> getAllRecords() {
        return list;
    }
}
