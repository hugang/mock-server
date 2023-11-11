package io.hugang.mock.dao;

import io.hugang.common.dao.BaseDao;
import io.hugang.mock.entity.TApiEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* api
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Mapper
public interface TApiDao extends BaseDao<TApiEntity> {
	
}
