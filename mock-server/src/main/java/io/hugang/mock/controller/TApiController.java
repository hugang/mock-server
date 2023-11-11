package io.hugang.mock.controller;

import io.hugang.common.page.PageResult;
import io.hugang.common.utils.Result;
import io.hugang.mock.entity.TAttachmentEntity;
import io.hugang.mock.service.TAttachmentService;
import io.hugang.mock.convert.TApiConvert;
import io.hugang.mock.entity.TApiEntity;
import io.hugang.mock.query.TApiQuery;
import io.hugang.mock.vo.TApiVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import io.hugang.mock.service.TApiService;
import org.apache.commons.lang3.ObjectUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* api
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@RestController
@RequestMapping("mock/api")
@Tag(name="api")
@AllArgsConstructor
public class TApiController {
    private final TApiService tApiService;
    private final TAttachmentService tAttachmentService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<TApiVO>> page(@ParameterObject @Valid TApiQuery query){
        PageResult<TApiVO> page = tApiService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<TApiVO> get(@PathVariable("id") Long id){
        TApiEntity entity = tApiService.getById(id);

        return Result.ok(TApiConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody TApiVO vo){
        tApiService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid TApiVO vo){
        if (!ObjectUtils.isEmpty(vo.getAttachmentId())){
            TAttachmentEntity attachmentEntity = tAttachmentService.getById(vo.getAttachmentId());
            vo.setAttachmentName(attachmentEntity.getName());
        }
        tApiService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Long> idList){
        tApiService.delete(idList);

        return Result.ok();
    }
}
