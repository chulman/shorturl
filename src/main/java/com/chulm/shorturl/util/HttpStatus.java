package com.chulm.shorturl.util;

public enum HttpStatus {

    Success(200), Redirect(301), Forbidden(403), NotFound(404), InternalServerError(500);

    private int num;

    HttpStatus(int num) {
        this.num = num;
    }

    public int getStatusCode() {
        return this.num;
    }

    public static HttpStatus valueOf(int num) {
        switch (num) {
            case 200:
                return Success;
            case 301:
                return Redirect;
            case 403:
                return Forbidden;
            case 404:
                return NotFound;
            case 500:
                return InternalServerError;
            default:
                return null;
        }
    }
}
