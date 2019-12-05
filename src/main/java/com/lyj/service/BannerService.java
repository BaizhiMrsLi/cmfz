package com.lyj.service;

import com.lyj.entity.Banner;

import java.util.List;

public interface BannerService {

    List<Banner> findAll(Integer start,Integer size);

    Integer findAllCount();

    Banner selectById(Banner banner);

    void save(Banner banner);

    void delete(Banner banner);

    void update(Banner banner);

    void deleteAll(List<String> ids);
}
