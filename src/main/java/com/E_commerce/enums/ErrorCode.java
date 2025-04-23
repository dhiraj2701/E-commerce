package com.E_commerce.enums;

public enum ErrorCode {

    // 1xx to 4xx related
    INVALID_INPUT(1, HttpStatusCode.BAD_REQUEST, "Invalid input provided"),
    UNAUTHORIZED_ACCESS(2, HttpStatusCode.UNAUTHORIZED, "Unauthorized access"),
    ACCESS_FORBIDDEN(3, HttpStatusCode.FORBIDDEN, "Access is forbidden"),
    RESOURCE_NOT_FOUND(4, HttpStatusCode.NOT_FOUND, "Requested resource not found"),
    METHOD_NOT_SUPPORTED(5, HttpStatusCode.METHOD_NOT_ALLOWED, "HTTP method not supported"),
    REQUEST_TIMEOUT_ERROR(6, HttpStatusCode.REQUEST_TIMEOUT, "Request timed out"),
    UNSUPPORTED_MEDIA(7, HttpStatusCode.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),
    TOO_MANY_REQUESTS_ERROR(8, HttpStatusCode.TOO_MANY_REQUESTS, "Too many requests"),

    // 5xx errors
    CANNOT_READ_FILE(9, HttpStatusCode.INTERNAL_SERVER_ERROR, "Cannot read file"),
    DATABASE_ERROR(10, HttpStatusCode.INTERNAL_SERVER_ERROR, "Database operation failed"),
    SERVICE_TEMPORARILY_UNAVAILABLE(11, HttpStatusCode.SERVICE_UNAVAILABLE, "Service temporarily unavailable"),
    GATEWAY_ERROR(12, HttpStatusCode.BAD_GATEWAY, "Bad gateway response"),
    TIMEOUT_REACHED(13, HttpStatusCode.GATEWAY_TIMEOUT, "Gateway timeout"),

    // Custom application-specific errors
    EMAIL_ALREADY_EXISTS(14, HttpStatusCode.CONFLICT, "Email already exists"),
    USERNAME_ALREADY_TAKEN(15, HttpStatusCode.CONFLICT, "Username is already taken"),
    PASSWORD_TOO_WEAK(16, HttpStatusCode.BAD_REQUEST, "Password is too weak"),
    TOKEN_EXPIRED(17, HttpStatusCode.UNAUTHORIZED, "Authentication token has expired"),
    INVALID_TOKEN(18, HttpStatusCode.UNAUTHORIZED, "Invalid authentication token"),
    FILE_TOO_LARGE(19, HttpStatusCode.PAYLOAD_TOO_LARGE, "Uploaded file is too large"),
    INVALID_FILE_TYPE(20, HttpStatusCode.UNSUPPORTED_MEDIA_TYPE, "Invalid file type"),
    TOO_EARLY_REQUEST(21, HttpStatusCode.TOO_EARLY, "Request sent too early"),
    RATE_LIMITED(22, HttpStatusCode.TOO_MANY_REQUESTS, "Rate limit exceeded"),
    INVALID_SESSION(23, HttpStatusCode.UNAUTHORIZED, "Session is invalid or expired"),
    DUPLICATE_REQUEST(24, HttpStatusCode.CONFLICT, "Duplicate request detected"),
    FAILED_DEPENDENCY_ERROR(25, HttpStatusCode.FAILED_DEPENDENCY, "Dependent resource failed");

    private final int code;
    private final HttpStatusCode httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatusCode httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return this.name();
    }

}
