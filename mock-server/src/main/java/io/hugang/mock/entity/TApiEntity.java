package io.hugang.mock.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * api
 *
 * @author hugang admin@hugang.io
 * @since 1.0.0 2023-11-11
 */

@Data
@TableName("t_api")
public class TApiEntity {
	/**
	* id
	*/
	@TableId
	private Integer id;

	/**
	* url
	*/
	private String url;

	/**
	* method
	*/
	private String method;

	/**
	* code
	*/
	private Integer code;

	/**
	* type
	*/
	private String type;

	/**
	* data
	*/
	private String data;

	/**
	* param
	*/
	private String param;

	/**
	* 添付ID
	*/
	private Integer attachmentId;

	/**
	* 添付名
	*/
	private String attachmentName;

}
