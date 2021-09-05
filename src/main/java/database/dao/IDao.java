package main.java.database.dao;

import java.util.List;

public interface IDao<T> {
    T get(Double id);
    List<T> getAll();
    boolean delete(T t);
}
