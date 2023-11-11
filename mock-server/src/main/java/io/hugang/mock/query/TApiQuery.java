package io.hugang.mock.query;

import io.hugang.common.query.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
* api查询
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "api查询")
public class TApiQuery extends Query {
    @Schema(description = "url")
    private String url;

    @Schema(description = "method")
    private String method;

}
