package ua.kiev.its.assertstruct.impl.config;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.config.ConfigBuilder;


@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class NodeConfig extends SubtreeConfig {

    boolean ordered;

    public NodeConfig(boolean ordered, boolean orderedDicts, boolean orderedLists, boolean ignoreUnknown) {
        super(orderedDicts, orderedLists, ignoreUnknown);
        this.ordered = ordered;
    }

    @Data
    public static class NodeConfigBuilder implements ConfigBuilder {
        private static final NodeConfigBuilder DEFAULT_BUILDER = new NodeConfigBuilder();
        Boolean ordered;
        Boolean ignoreUnknown;

        @Override
        public NodeConfigBuilder set(String name, Object value) {
            if (name.equals("ordered")) {
                ordered = (Boolean) value;
            } else if (name.equals("ignoreUnknown")) {
                ignoreUnknown = (Boolean) value;
            } else {
                throw new IllegalArgumentException("Unknown property: " + name + " at NodeConfig");
            }
            return this;
        }

        public NodeConfig build(SubtreeConfig parent, boolean isDict) {
            return new NodeConfig(
                    ordered == null ? (isDict ? parent.orderedDicts : parent.orderedLists) : ordered,
                    parent.orderedDicts,
                    parent.orderedLists,
                    ignoreUnknown == null ? parent.ignoreUnknown : ignoreUnknown);
        }

        public static NodeConfig buildDefault(SubtreeConfig parent, boolean isDict) {
            return DEFAULT_BUILDER.build(parent, isDict);
        }
    }
}
