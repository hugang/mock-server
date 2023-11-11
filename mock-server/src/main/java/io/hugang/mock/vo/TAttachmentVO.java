package io.hugang.mock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

/**
* 添付ファイル管理
*
* @author hugang admin@hugang.io
* @since 1.0.0 2023-11-11
*/
@Data
@Schema(description = "添付ファイル管理")
public class TAttachmentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "添付ファイル名称")
	private String name;

	@Schema(description = "添付ファイルアドレス")
	private String url;

	@Schema(description = "添付ファイルサイズ")
	private Long size;


}
