package io.hugang.mock.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.hugang.common.page.PageResult;
import io.hugang.common.service.impl.BaseServiceImpl;
import io.hugang.mock.convert.TApiConvert;
import io.hugang.mock.dao.TApiDao;
import io.hugang.mock.entity.TApiEntity;
import io.hugang.mock.query.TApiQuery;
import io.hugang.mock.service.TApiService;
import io.hugang.mock.vo.TApiVO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * api
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */
@Service
@AllArgsConstructor
public class TApiServiceImpl extends BaseServiceImpl<TApiDao, TApiEntity> implements TApiService {

    @Override
    public PageResult<TApiVO> page(TApiQuery query) {
        IPage<TApiEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TApiConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TApiEntity> getWrapper(TApiQuery query){
        LambdaQueryWrapper<TApiEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(query.getUrl()), TApiEntity::getUrl, query.getUrl());
        wrapper.eq(StringUtils.isNotEmpty(query.getMethod()), TApiEntity::getMethod, query.getMethod());
        return wrapper;
    }

    @Override
    public void save(TApiVO vo) {
        TApiEntity entity = TApiConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TApiVO vo) {
        TApiEntity entity = TApiConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}
