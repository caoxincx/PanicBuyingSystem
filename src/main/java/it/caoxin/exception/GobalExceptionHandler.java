package it.caoxin.exception;

import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@ControllerAdvice
@ResponseBody
public class GobalExceptionHandler {

    @ExceptionHandler
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        e.printStackTrace();

        if (e instanceof  GobalException){
            GobalException exception = (GobalException) e;
            return Result.error(exception.getCodeMsg());
        } else if(e instanceof BindException){
            BindException exception = (BindException) e;
            List<ObjectError> allErrors = exception.getAllErrors();
            ObjectError error =allErrors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillsArgs(msg));
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
