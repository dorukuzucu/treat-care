package main.java.database.dao;

import java.util.List;

public interface IDao<T> {
    T get(Double id);
    List<T> getAll();
    boolean update(T t, String[] params);
    boolean delete(T t);
}
