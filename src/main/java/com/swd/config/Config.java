package com.swd.config;

import com.mongodb.MongoClientURI;

public final class Config {
    // Database
    public final static MongoClientURI ConnectionString = new MongoClientURI("mongodb://127.0.0.1:27017");
    public final static String DbName = "swd";
    // MultipartResolver
    public final static Integer MultipartResolverMaxUploadSize = 104857600;
    public final static Integer MultipartResolverMaxInMemorySize = 104857600;
    public final static String MultipartResolverDefaultEncoding = "utf-8";
    // InternalResourceViewResolver
    public final static String InternalResourceViewResolverPrefix = "/WEB-INF/jsp/";
    public final static String InternalResourceViewResolverSuffix = ".jsp";
    // BCryptPasswordEncoderStrength
    public final static Integer BCryptPasswordEncoderStrength = 11;
    // Uploads
    public final static String BaseUploadDir = "uploads";
}
