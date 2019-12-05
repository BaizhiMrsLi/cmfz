package com.lyj.service;

import com.lyj.entity.Guru;

import java.util.List;

public interface GuruService {
    List<Guru> findAll(Integer start, Integer size);

    Integer findAllCount();

    Guru selectById(Guru guru);

    void save(Guru guru);

    void delete(Guru guru);

    void update(Guru guru);

    void deleteAll(List<String> ids);

    List<Guru> findAllGuru();
}
