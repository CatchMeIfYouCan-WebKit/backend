package com.team.webkit.backend.support;

import static com.team.webkit.backend.support.MspUtil.makeResult;

import com.team.webkit.backend.support.annotation.MSP;
import com.team.webkit.backend.support.protocol.MspResult;
import com.team.webkit.backend.support.protocol.MspStatus;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = {MSP.class})
public class MspHandler {

    @ExceptionHandler
    public ResponseEntity<MspResult> globalExceptionHandler(Exception e) {
        log.error("Exception occurred.", e);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("HandlerMsg", e.getMessage());

        MspResult result = makeResult(MspStatus.ERROR, body);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MspResult> handleConstraintViolationException(
        ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
            .map(violation -> violation.getMessageTemplate())
            .findFirst()
            .orElse("입력값이 올바르지 않습니다.");

        Map<String, String> body = new LinkedHashMap<>();
        body.put("rsltCode", "1001");
        body.put("rsltMsg", msg);

        MspResult result = makeResult(MspStatus.OK, body);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}