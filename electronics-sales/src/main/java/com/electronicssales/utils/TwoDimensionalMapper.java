package com.electronicssales.utils;

public interface TwoDimensionalMapper<T, S> extends Mapper<T, S> {

    S secondMapping(T arg);
    
}