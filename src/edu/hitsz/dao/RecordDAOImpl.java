package edu.hitsz.dao;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class RecordDAOImpl implements RecordDAO{
    //实现接口的类
    private static List<Record> list;

    //记录记录文件地址
    private static String recordPath="E:/record.dat";

    static{
        //初始化静态列表
        Path path= Paths.get(recordPath);
        if(!Files.exists(path)){
            //如果对应文件不存在
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file=new File(recordPath);
        FileInputStream fips=null;
        try {
            fips=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("打开文件输入流失败");
        }
        ObjectInputStream oips=null;
        try{
            oips=new ObjectInputStream(fips);
        }catch(Exception e){
            System.out.println("打开对象输入流失败");
        }
        list=new ArrayList<>();
        do{
            try{
                Record add=(Record)oips.readObject();
                list.add(add);
            }catch(Exception e){
                break;
            }
        }while(true);

    }





    @Override
    public void doAdd(Record record) throws IOException {
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
        File file=new File(recordPath);
        FileOutputStream fops=new FileOutputStream(file);
        ObjectOutputStream oops=new ObjectOutputStream(fops);
        List<Record> cur=getAllRecords();
        for(Record recorda:cur) {
            oops.writeObject(recorda);
        }
        oops.close();
        fops.close();
    }


    @Override
    public List<Record> getAllRecords() throws IOException {
        //读取文件，写入
        return list;
    }



}
