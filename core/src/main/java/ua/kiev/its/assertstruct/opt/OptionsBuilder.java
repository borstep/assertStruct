package ua.kiev.its.assertstruct.opt;

import ua.kiev.its.assertstruct.impl.opt.NodeOptions;

public interface OptionsBuilder {
    NodeOptions.NodeOptionsBuilder set(String name, Object value);
}
