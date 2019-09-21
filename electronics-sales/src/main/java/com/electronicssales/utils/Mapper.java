package com.electronicssales.utils;

/**
 * Mapper
 */
public interface Mapper<T, S> {

    T mapping(S args);
    
}