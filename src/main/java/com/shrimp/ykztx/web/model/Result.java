package com.shrimp.ykztx.web.model;

public class Result<T> {
	private Header header;
	private T body;

	public Result(T body) {
		this(null, null, true, body);
	}

	public Result(String message, T body) {
		this(null, message, true, body);
	}

	public Result(String errorCode, String message) {
		this(errorCode, message, false, null);
	}

	public Result(String errorCode, String message, boolean success, T body) {
		super();
		this.header = new Header(errorCode, message, success);
		this.body = body;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
	public void setErrorCode(String errorCode) {
		this.header.errorCode = errorCode;
	}


	public void setMessage(String message) {
		this.header.message = message;
	}

	public class Header {
		private String errorCode;
		private String message;
		private boolean success;

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public Header(String errorCode, String message, boolean success) {
			super();
			this.errorCode = errorCode;
			this.message = message;
			this.success = success;
		}

	}
}
