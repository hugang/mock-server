package io.hugang.mock.query;

import io.hugang.common.query.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
* 添付ファイル管理查询
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "添付ファイル管理查询")
public class TAttachmentQuery extends Query {
    @Schema(description = "添付ファイル名称")
    private String name;

}
