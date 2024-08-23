package org.assertstruct.impl.opt;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.assertstruct.opt.OptionsBuilder;


@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class NodeOptions extends SubtreeOptions {

    boolean ordered;

    public NodeOptions(boolean ordered, boolean orderedDicts, boolean orderedLists, boolean ignoreUnknown) {
        super(orderedDicts, orderedLists, ignoreUnknown);
        this.ordered = ordered;
    }

    @Data
    public static class NodeOptionsBuilder implements OptionsBuilder {
        private static final NodeOptionsBuilder DEFAULT_BUILDER = new NodeOptionsBuilder();
        Boolean ordered;
        Boolean ignoreUnknown;

        @Override
        public NodeOptionsBuilder set(String name, Object value) {
            if (name.equals("ordered")) {
                ordered = (Boolean) value;
            } else if (name.equals("ignoreUnknown")) {
                ignoreUnknown = (Boolean) value;
            } else {
                throw new IllegalArgumentException("Unknown property: " + name + " at NodeConfig");
            }
            return this;
        }

        public NodeOptions build(SubtreeOptions parent, boolean isDict) {
            return new NodeOptions(
                    ordered == null ? (isDict ? parent.orderedDicts : parent.orderedLists) : ordered,
                    parent.orderedDicts,
                    parent.orderedLists,
                    ignoreUnknown == null ? parent.ignoreUnknown : ignoreUnknown);
        }

        public static NodeOptions buildDefault(SubtreeOptions parent, boolean isDict) {
            return DEFAULT_BUILDER.build(parent, isDict);
        }
    }
}
