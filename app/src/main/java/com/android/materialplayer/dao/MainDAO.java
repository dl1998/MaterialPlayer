package com.android.materialplayer.dao;

import java.util.List;

/**
 * Created by dl1998 on 07.12.17.
 */

public interface MainDAO<T> {

    void add(T object, String SQL);

    void update(T object, String SQL);

    T findById(String selection, Long id);

    List<T> getAll();

    List<T> getAllBy(String selection, String[] selectionArgs);

    List<T> getAllInOrder(String orderBy);

}
