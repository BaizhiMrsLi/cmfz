package com.lyj.dao;

import com.lyj.entity.Album;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface AlbumDao extends Mapper<Album>, DeleteByIdListMapper<Album,String> {
}
