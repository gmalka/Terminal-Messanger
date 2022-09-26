package com.repositories;

import java.util.List;

public interface Repository<T> {
    final String CREATE_SERVER = "CREATE SCHEMA IF NOT EXISTS server;";
    public T findById(int id);
    public List<T> getAll();
    public void add(T element);
    public void update(T element);
    public void delete(Long id);
    void deleteByName(String name);
 }
