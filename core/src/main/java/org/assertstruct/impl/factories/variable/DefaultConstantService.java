package org.assertstruct.impl.factories.variable;

import lombok.Value;
import org.assertstruct.impl.factories.variable.matcher.AnyList;
import org.assertstruct.impl.factories.variable.matcher.AnyObject;
import org.assertstruct.matcher.ValueMatcher;
import org.assertstruct.service.ConstantService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Value
public class DefaultConstantService implements ConstantService {

    Map<String, Object> constants;

    public DefaultConstantService() {
        HashMap<String, Object> defaultConstants = new HashMap<>();
        defaultConstants.put(AnyList.NAME, new AnyList());
        defaultConstants.put(AnyObject.NAME, new AnyObject());
        constants = Collections.synchronizedMap(defaultConstants);
    }

    @Override
    public void addConstant(String name, Object value) {
        constants.put(name, value);
    }

    @Override
    public void addMatcher(String name, ValueMatcher matcher) {
        constants.put(name, matcher);
    }

    @Override
    public Object getConstant(String name) {
        return constants.get(name);
    }

}
