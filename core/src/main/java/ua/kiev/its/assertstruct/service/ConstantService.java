package ua.kiev.its.assertstruct.service;

import ua.kiev.its.assertstruct.matcher.ValueMatcher;

public interface ConstantService {
    void addConstant(String name, Object value);

    void addMatcher(String name, ValueMatcher matcher);

    Object getConstant(String name);
}
