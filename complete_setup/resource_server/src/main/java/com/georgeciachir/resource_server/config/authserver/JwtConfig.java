package com.georgeciachir.resource_server.config.authserver;

public interface JwtConfig {

    JwtDecodingData getJwtDecodingData();

    String getIssuerUrl();
}
