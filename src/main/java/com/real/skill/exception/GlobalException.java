package com.real.skill.exception;

import com.real.skill.result.CodeMsg;

/**
 * @author: mabin
 * @create: 2019/5/16 11:30
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 8027183374940520019L;
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
