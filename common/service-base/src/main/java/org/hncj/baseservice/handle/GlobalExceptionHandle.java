package org.hncj.baseservice.handle;

import org.hncj.commonutil.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(OeException.class)
    @ResponseBody
    public R error(OeException e) {
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
