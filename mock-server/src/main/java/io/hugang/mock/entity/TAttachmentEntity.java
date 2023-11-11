package io.hugang.mock.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 添付ファイル管理
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */

@Data
@TableName("t_attachment")
public class TAttachmentEntity {
	/**
	* id
	*/
	@TableId
	private Long id;

	/**
	* 添付ファイル名称
	*/
	private String name;

	/**
	* 添付ファイルアドレス
	*/
	private String url;

	/**
	* 添付ファイルサイズ
	*/
	private Long size;

}
