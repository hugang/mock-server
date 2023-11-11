package io.hugang.mock.service;

import io.hugang.common.page.PageResult;
import io.hugang.common.service.BaseService;
import io.hugang.mock.vo.TAttachmentVO;
import io.hugang.mock.query.TAttachmentQuery;
import io.hugang.mock.entity.TAttachmentEntity;

import java.util.List;

/**
 * 添付ファイル管理
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */
public interface TAttachmentService extends BaseService<TAttachmentEntity> {

    PageResult<TAttachmentVO> page(TAttachmentQuery query);

    void save(TAttachmentVO vo);

    void update(TAttachmentVO vo);

    void delete(List<Long> idList);
}
