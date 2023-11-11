package io.hugang.mock.convert;

import io.hugang.mock.entity.TApiEntity;
import io.hugang.mock.vo.TApiVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* api
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Mapper
public interface TApiConvert {
    TApiConvert INSTANCE = Mappers.getMapper(TApiConvert.class);

    TApiEntity convert(TApiVO vo);

    TApiVO convert(TApiEntity entity);

    List<TApiVO> convertList(List<TApiEntity> list);

}
