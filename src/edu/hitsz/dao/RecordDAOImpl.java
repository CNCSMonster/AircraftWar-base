package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordDAOImpl implements RecordDAO{
    //实现接口的类
    private List<Record> list;

    //记录记录文件地址
    private static String recordPath="E:/rec.dat";

    public RecordDAOImpl() throws IOException {
        list=getAllRecords();
    }



    @Override
    public void doAdd(Record record) throws IOException {

        if(list==null) {
            list = getAllRecords();
        }
        list.add(record);
        //然后排序
        list.sort(new Comparator<Record>() {
                      @Override
                      public int compare(Record o1, Record o2) {
                          int sa=o1.getScore();
                          int sb=o2.getScore();
                          return sb-sa;
                      }
                  }
        );
        //把list写入文件
        FileOutputStream fops=new FileOutputStream(recordPath);
        ObjectOutputStream oops=new ObjectOutputStream(fops);
        for(Record recorda:list){
            oops.writeObject(recorda);
        }
        oops.close();
        fops.close();
    }




    @Override
    public List<Record> getAllRecords() throws IOException {
        //读取文件，写入
        if(list==null){
            list=new ArrayList<>();
            FileInputStream fips=new FileInputStream(recordPath);
            ObjectInputStream oips=new ObjectInputStream(fips);
            do{
                Record add;
                try{
                    add=(Record)oips.readObject();
                }catch(Exception e){
                    break;
                }
                list.add(add);
            }while(true);
            //排序
            list.sort(new Comparator<Record>() {
                          @Override
                          public int compare(Record o1, Record o2) {
                              int sa=o1.getScore();
                              int sb=o2.getScore();
                              return sb-sa;
                          }
                      }
            );
            oips.close();
            fips.close();
            return list;
        }else{
            return list;
        }
    }
}
