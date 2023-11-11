package io.hugang.mock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

/**
* api
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Data
@Schema(description = "api")
public class TApiVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Integer id;

	@Schema(description = "url")
	private String url;

	@Schema(description = "method")
	private String method;

	@Schema(description = "code")
	private Integer code;

	@Schema(description = "type")
	private String type;

	@Schema(description = "data")
	private String data;

	@Schema(description = "param")
	private String param;

	@Schema(description = "添付ID")
	private Integer attachmentId;

	@Schema(description = "添付名")
	private String attachmentName;


}
