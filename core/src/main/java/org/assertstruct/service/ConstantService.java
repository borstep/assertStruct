package org.assertstruct.service;

import org.assertstruct.matcher.ValueMatcher;

public interface ConstantService {
    void addConstant(String name, Object value);

    void addMatcher(String name, ValueMatcher matcher);

    Object getConstant(String name);
}
