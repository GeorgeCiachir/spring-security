package com.georgeciachir.resource_server.controller;

import java.util.Map;

public record HelloDto(String message, String issuer, String access_token, Map<String, Object> claims) {
}
