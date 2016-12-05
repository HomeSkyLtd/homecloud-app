package com.homesky.homecloud_lib.exceptions;

/**
 * This exception is thrown when the library is called without being properly initialized. This includes
 * setting the username, the password and the target url.
 */
public class NotProperlyInitializedException extends RuntimeException {
}
