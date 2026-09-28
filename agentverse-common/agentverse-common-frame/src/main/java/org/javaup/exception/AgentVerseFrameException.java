package org.javaup.exception;

import org.javaup.common.ApiResponse;
import org.javaup.enums.BaseCode;
import lombok.Data;

/**
 * @description: 异常类
 * @author: slienceecheo
 **/

@Data
public class AgentVerseFrameException extends BaseException {

	private Integer code;

	private String message;

	public AgentVerseFrameException() {
		super();
	}

	public AgentVerseFrameException(String message) {
		super(message);
	}

	public AgentVerseFrameException(String code, String message) {
		super(message);
		this.code = Integer.parseInt(code);
		this.message = message;
	}

	public AgentVerseFrameException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public AgentVerseFrameException(BaseCode baseCode) {
		super(baseCode.getMsg());
		this.code = baseCode.getCode();
		this.message = baseCode.getMsg();
	}

	public AgentVerseFrameException(ApiResponse apiResponse) {
		super(apiResponse.getMessage());
		this.code = apiResponse.getCode();
		this.message = apiResponse.getMessage();
	}

	public AgentVerseFrameException(Throwable cause) {
		super(cause);
	}

	public AgentVerseFrameException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public AgentVerseFrameException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.message = message;
	}
}
