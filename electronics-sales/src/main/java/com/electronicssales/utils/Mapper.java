package com.electronicssales.utils;

/**
 * Mapper
 */
public interface Mapper<T, S> {

    T map(S args);
    
}