package com.electronicssales.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

public class AppUtil {

    public static <E, T> List<E> parseListFrom(Page<T> page, Mapper<E, T> mapper) {
        return page
            .getContent()
            .stream()
            .map(mapper::mapping)
            .collect(Collectors.toList());
    }
}