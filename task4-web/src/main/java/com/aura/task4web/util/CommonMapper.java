package com.aura.task4web.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Mybatis通用接口
 * @param <T>
 */
public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
