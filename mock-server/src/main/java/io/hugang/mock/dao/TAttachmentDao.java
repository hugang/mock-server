package io.hugang.mock.dao;

import io.hugang.common.dao.BaseDao;
import io.hugang.mock.entity.TAttachmentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 添付ファイル管理
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Mapper
public interface TAttachmentDao extends BaseDao<TAttachmentEntity> {
	
}
