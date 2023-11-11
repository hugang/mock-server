package io.hugang.mock.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.hugang.common.page.PageResult;
import io.hugang.common.service.impl.BaseServiceImpl;
import io.hugang.mock.convert.TAttachmentConvert;
import io.hugang.mock.dao.TAttachmentDao;
import io.hugang.mock.entity.TAttachmentEntity;
import io.hugang.mock.query.TAttachmentQuery;
import io.hugang.mock.service.TAttachmentService;
import io.hugang.mock.vo.TAttachmentVO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 添付ファイル管理
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */
@Service
@AllArgsConstructor
public class TAttachmentServiceImpl extends BaseServiceImpl<TAttachmentDao, TAttachmentEntity> implements TAttachmentService {

    @Override
    public PageResult<TAttachmentVO> page(TAttachmentQuery query) {
        IPage<TAttachmentEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TAttachmentConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TAttachmentEntity> getWrapper(TAttachmentQuery query){
        LambdaQueryWrapper<TAttachmentEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(query.getName()), TAttachmentEntity::getName, query.getName());
        return wrapper;
    }

    @Override
    public void save(TAttachmentVO vo) {
        TAttachmentEntity entity = TAttachmentConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TAttachmentVO vo) {
        TAttachmentEntity entity = TAttachmentConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}
