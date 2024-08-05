package ua.kiev.its.assertstruct.config;

import ua.kiev.its.assertstruct.impl.config.NodeConfig;

public interface ConfigBuilder {
    NodeConfig.NodeConfigBuilder set(String name, Object value);
}
