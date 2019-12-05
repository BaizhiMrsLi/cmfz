package com.lyj.service;

import com.lyj.entity.Album;

import java.util.List;

public interface AlbumService {
    List<Album> findAll(Integer start, Integer size);

    Integer findAllCount();

    List<Album> queryAll();

    Album selectById(Album album);

    void save(Album album);

    void delete(Album album);

    void update(Album album);

    void deleteAll(List<String> ids);
}
