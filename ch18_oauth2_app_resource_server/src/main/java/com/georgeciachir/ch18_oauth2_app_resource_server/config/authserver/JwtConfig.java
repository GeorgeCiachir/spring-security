package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

public interface JwtConfig {

    JwtDecodingData getJwtDecodingData();

    String getIssuerUrl();
}
