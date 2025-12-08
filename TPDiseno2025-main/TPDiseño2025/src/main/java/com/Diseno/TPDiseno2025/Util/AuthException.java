package com.Diseno.TPDiseno2025.Util;

public class AuthException extends RuntimeException{
    public AuthException(String msg){
        super(msg);
    }

    public String getMessage(){
        return super.getMessage();
    }
}
