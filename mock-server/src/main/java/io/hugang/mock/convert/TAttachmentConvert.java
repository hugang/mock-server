package io.hugang.mock.convert;

import io.hugang.mock.entity.TAttachmentEntity;
import io.hugang.mock.vo.TAttachmentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 添付ファイル管理
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Mapper
public interface TAttachmentConvert {
    TAttachmentConvert INSTANCE = Mappers.getMapper(TAttachmentConvert.class);

    TAttachmentEntity convert(TAttachmentVO vo);

    TAttachmentVO convert(TAttachmentEntity entity);

    List<TAttachmentVO> convertList(List<TAttachmentEntity> list);

}
