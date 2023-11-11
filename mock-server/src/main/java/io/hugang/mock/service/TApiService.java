package io.hugang.mock.service;

import io.hugang.common.page.PageResult;
import io.hugang.common.service.BaseService;
import io.hugang.mock.vo.TApiVO;
import io.hugang.mock.query.TApiQuery;
import io.hugang.mock.entity.TApiEntity;

import java.util.List;

/**
 * api
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */
public interface TApiService extends BaseService<TApiEntity> {

    PageResult<TApiVO> page(TApiQuery query);

    void save(TApiVO vo);

    void update(TApiVO vo);

    void delete(List<Long> idList);
}
