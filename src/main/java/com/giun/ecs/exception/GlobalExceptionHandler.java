package com.giun.ecs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.giun.ecs.dto.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;

//@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(
			UsernameNotFoundException ex) {
		return buildErrorResponse("使用者不存在", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
			AccessDeniedException ex) {
		return buildErrorResponse("您沒有權限進行此操作", HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationError(
			MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream().map(
				error -> error.getField() + ": " + error.getDefaultMessage())
				.findFirst().orElse("參數驗證失敗");
		return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
			ConstraintViolationException ex) {
		return buildErrorResponse("資料驗證錯誤: " + ex.getMessage(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleRuntime(
			RuntimeException ex) {
		return buildErrorResponse("系統錯誤: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
		return buildErrorResponse("未知錯誤: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ApiResponse<Void>> buildErrorResponse(String message,
			HttpStatus status) {
		return ResponseEntity.status(status)
				.body(ApiResponse.error(message, status));
	}

	// @ExceptionHandler(ApplicationException.class)
	// public ResponseEntity<ApiResponse<Void>> handleApplication(
	// ApplicationException ex) {
	// ApiResponse<Void> response = ApiResponse.error(ex.getMessage(),
	// HttpStatus.BAD_REQUEST);
	// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	// }

	// @ExceptionHandler(RuntimeException.class)
	// public ResponseEntity<ApiResponse<Void>> handleRuntime(
	// RuntimeException ex) {
	// ApiResponse<Void> response = ApiResponse.error(ex.getMessage(),
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// .body(response);
	// }

	// 可加入更多 Handler 針對不同 Exception 類型
}
