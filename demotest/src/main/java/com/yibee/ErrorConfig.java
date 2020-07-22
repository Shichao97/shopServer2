package com.yibee;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorConfig implements ErrorPageRegistrar {


    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error404Page=new ErrorPage(HttpStatus.NOT_FOUND,"/index.html" );
        //ErrorPage error401Page=new ErrorPage(HttpStatus.UNAUTHORIZED,"/error401");
        //ErrorPage error500Page=new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/error500");
        registry.addErrorPages(error404Page);
    }
    
}