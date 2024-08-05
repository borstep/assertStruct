package ua.kiev.its.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.config.ConfigTemplateKey;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ObjectNode extends LinkedHashMap<String, TemplateNode> implements StructTemplateNode, DataNode<Map<String, Object>> {

    @Delegate
    StructuredTemplateNodeShared shared;

    Map<String, TemplateNode> simpleNodes = new HashMap<>();
    Map<String, TemplateNode> matchedNodes = new HashMap<>();

    public ObjectNode(TemplateKey key, ExtToken startToken) {
        super();
        this.shared = new StructuredTemplateNodeShared(key, startToken, this.values());
    }

    @Override
    public void printDebug(StringBuilder out) throws IOException {
        if (shared.startToken != null)
            shared.startToken.print(out, false, false);
        else
            out.append("{\n");
        for (TemplateNode node : values()) {
            node.getKey().getToken().print(out, false, false);
            node.printDebug(out);
        }
        if (getToken() != null)
            getToken().print(out, false, false);
        else
            out.append("},\n");
    }


    /**
     * Look for best template key for actual key
     *
     * @param actualKey
     * @param context
     * @return
     */
    public TemplateNode lookup(String actualKey, Matcher context) { // TODO optimize
        TemplateKey matcherKey = null;
        TemplateNode templateNode = simpleNodes.get(actualKey);
        if (templateNode != null) {
            return templateNode;
        }
        for (TemplateNode node : matchedNodes.values()) {
            if (node.getKey().getType() == TemplateKeyType.MATCHER) {
                if (((MatcherTemplateKey) node.getKey()).match(actualKey, context)) {
                    return node;
                }
            }
        }
        return null;
    }

    @Override
    public TemplateNode put(String keyValue, TemplateNode value) {
        TemplateKey key = value.getKey();
        if (key.getType() == TemplateKeyType.SIMPLE) {
            simpleNodes.put(keyValue, value);
        } else if (key.getType() == TemplateKeyType.MATCHER) {
            matchedNodes.put(keyValue, value);
        } else if (key.getType().isConfig()) {
            if (key.getType().isSubtreeConfig()) {
                shared.addSubtreeConfig(((ConfigTemplateKey) key).getPropertyName(), value);
            } else {
                shared.addConfig(((ConfigTemplateKey) key).getPropertyName(), value);
            }
        }
        return super.put(keyValue, value);
    }


    @Override
    public TemplateNode getByKey(Object key) {
        return simpleNodes.get(key);
    }

    public boolean containsTemplateKey(TemplateKey templateKey) {
        return containsKey(templateKey.getValue());
    }

    @Override
    public Map<String, Object> toData() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>(size());
        for (TemplateNode child : values()) {
            if (child.getKey() != null && child.getKey().getType() == TemplateKeyType.SIMPLE && child.isDataNode())
                map.put(child.getKey().getValue(), ((DataNode<Object>) child).toData());
        }
        return map;
    }
}
