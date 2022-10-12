package com.revature.utils;

import java.util.List;

public interface CrudDaoInterface<T> {
    //doa - Data Access Object
    //pattern that provided and abstract interface to some type of database or other persistence mechanism
    // returns in to reference primary key
    int create(T t);

    List<T> getAll();

    T getByID(int id);

    T update(T t);

    boolean delete(T t);
}
