package com.nagarro.studentapi.facade.converters;

public interface Converter<T1, T2> {
    T2 toDto(T1 model);

    T1 toModel(T2 dto);
}
