package org.assertstruct.opt;

import org.assertstruct.impl.opt.NodeOptions;

public interface OptionsBuilder {
    NodeOptions.NodeOptionsBuilder set(String name, Object value);
}
