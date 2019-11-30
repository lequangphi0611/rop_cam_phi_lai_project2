package com.electronicssales.models;

public interface ProductNameAndIdOnly {

    long getId();

    String getName();

    long[] getCategoriesId();
}