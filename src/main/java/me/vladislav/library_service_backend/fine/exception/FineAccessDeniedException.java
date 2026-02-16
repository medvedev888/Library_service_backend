package me.vladislav.library_service_backend.fine.exception;

import org.springframework.http.HttpStatus;

public class FineAccessDeniedException extends FineException {
  public FineAccessDeniedException() {
    super(HttpStatus.FORBIDDEN, "Нет прав на выполнение операции со штрафом");
  }
}
