package it.caoxin.exception;

import it.caoxin.result.CodeMsg;

public class GobalException extends RuntimeException {
    private CodeMsg codeMsg;

    public GobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg(){
        return codeMsg;
    }
}
