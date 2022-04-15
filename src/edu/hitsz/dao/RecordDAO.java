package edu.hitsz.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface RecordDAO {
    public abstract void doAdd(Record record) throws IOException;
    public abstract List<Record> getAllRecords() throws IOException;
}
