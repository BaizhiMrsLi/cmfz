package com.lyj.service;

import com.lyj.dao.CounterDao;
import com.lyj.entity.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CounterServiceImpl implements CounterService {
    @Autowired
    private CounterDao counterDao;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Counter> selCounterByChange(Counter counter) {
        return counterDao.select(counter);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Counter> queryAll() {
        return counterDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Counter selCounterById(Counter counter) {
        return counterDao.selectByPrimaryKey(counter);
    }

    @Override
    public void save(Counter counter) {
        counterDao.insert(counter);
    }

    @Override
    public void delete(Counter counter) {
        counterDao.delete(counter);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void update(Counter counter) {
        counterDao.updateByPrimaryKeySelective(counter);
    }
}
