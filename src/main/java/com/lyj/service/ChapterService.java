package com.lyj.service;

import com.lyj.entity.Chapter;

import java.util.List;

public interface ChapterService {
    List<Chapter> findAll(Chapter chapter,Integer start, Integer size);

    Integer findAllCount(Chapter chapter);

    Chapter selectById(Chapter chapter);

    void save(Chapter chapter);

    void delete(Chapter chapter);

    void update(Chapter chapter);

    void deleteAll(List<String> ids);
}
