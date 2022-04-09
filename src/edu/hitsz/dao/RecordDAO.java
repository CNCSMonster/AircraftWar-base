package edu.hitsz.dao;

import java.util.List;

public interface RecordDAO {
    public abstract void doAdd(Record record);
    public abstract List<Record> getAllRecords();
}
