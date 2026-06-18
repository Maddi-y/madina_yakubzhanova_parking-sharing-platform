package com.epam.capstone.validation;

public interface Validator<T> {

    void validate(T object);
}
