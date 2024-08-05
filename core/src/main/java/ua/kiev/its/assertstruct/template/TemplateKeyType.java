package ua.kiev.its.assertstruct.template;

public enum TemplateKeyType {
    SIMPLE,
    EVALUATOR,
    MATCHER,
    CONFIG_NODE,
    CONFIG_FIELD,
    CONFIG_SUB_TREE,
    CONFIG_SUB_TREE_FIELD;

    public boolean isConfig() {
        return this == CONFIG_NODE || this == CONFIG_FIELD || this == CONFIG_SUB_TREE || this == CONFIG_SUB_TREE_FIELD;
    }

    public boolean isSubtreeConfig() {
        return this == CONFIG_SUB_TREE || this == CONFIG_SUB_TREE_FIELD;
    }
}
