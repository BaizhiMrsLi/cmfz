package com.lyj.service;

import com.lyj.entity.Counter;

import java.util.List;

public interface CounterService {
    List<Counter> selCounterByChange(Counter counter);

    List<Counter> queryAll();

    Counter selCounterById(Counter counter);

    void save(Counter counter);

    void delete(Counter counter);

    void update(Counter counter);
}
