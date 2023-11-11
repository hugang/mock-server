package io.hugang.mock.controller;

import io.hugang.common.page.PageResult;
import io.hugang.common.utils.Result;
import io.hugang.mock.convert.TAttachmentConvert;
import io.hugang.mock.entity.TAttachmentEntity;
import io.hugang.mock.query.TAttachmentQuery;
import io.hugang.mock.service.TAttachmentService;
import io.hugang.mock.vo.TAttachmentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 添付ファイル管理
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */
@RestController
@RequestMapping("mock/attachment")
@Tag(name = "添付ファイル管理")
public class TAttachmentController {
    private final TAttachmentService tAttachmentService;

    @Value("${storage.path}")
    private String storagePath;

    public TAttachmentController(TAttachmentService tAttachmentService) {
        this.tAttachmentService = tAttachmentService;
    }

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<TAttachmentVO>> page(@ParameterObject @Valid TAttachmentQuery query) {
        PageResult<TAttachmentVO> page = tAttachmentService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<TAttachmentVO> get(@PathVariable("id") Long id) {
        TAttachmentEntity entity = tAttachmentService.getById(id);

        return Result.ok(TAttachmentConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestParam("file") MultipartFile file) throws IOException {

        TAttachmentVO vo = new TAttachmentVO();
        vo.setName(file.getOriginalFilename());
        vo.setSize(file.getSize());

        // save file to local path
        String absolutePath = storagePath + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(absolutePath));

        vo.setUrl(absolutePath);
        tAttachmentService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid TAttachmentVO vo) {
        tAttachmentService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Long> idList) {
        tAttachmentService.delete(idList);

        return Result.ok();
    }
}
