package com.homesky.homecloud_lib.exceptions;

/**
 * This exception is thrown if there was a network error when making a request.
 */
public class NetworkException extends Exception {
    public NetworkException(String msg) {
        super(msg);
    }
}
