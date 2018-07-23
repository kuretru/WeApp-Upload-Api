package com.jhkj.weapp.upload.controller;

import com.jhkj.weapp.common.controller.BaseController;
import com.jhkj.weapp.common.entity.ApiResponse;
import com.jhkj.weapp.common.exception.InvalidParametersException;
import com.jhkj.weapp.common.exception.MissingParametersException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@ControllerAdvice
@ResponseBody
public class ExceptionController extends BaseController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingParametersException.class)
    public ApiResponse missingParametersExceptionHandler(MissingParametersException e) {
        return ApiResponse.missingParameters(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParametersException.class)
    public ApiResponse invalidParametersExceptionHandler(InvalidParametersException e) {
        return ApiResponse.missingParameters(e.getMessage());
    }

}
