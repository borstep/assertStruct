package org.assertstruct.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.result.RootResult;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.template.node.ArrayNode;
import org.assertstruct.template.node.ObjectNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Template {
    AssertStructService env;
    TemplateNode root;

    public Template(TemplateNode root, AssertStructService env) {
        this.root = root;
        this.env = env;
        if (root instanceof StructTemplateNode) {
            ((StructTemplateNode) root).sealConfigs(env.getSubtreeOptions());
        }
    }

    public RootResult match(Object value) {
        return new Matcher(env, this).match(value);
    }

    public void printDebug(StringBuilder builder) throws IOException {
        root.printDebug(builder);
    }

    public String asString() {
        StringBuilder out = new StringBuilder();
        root.print(out, false, false, 0, false);
        return out.toString();
    }

    StructTemplateNode asStruct() {
        return (StructTemplateNode) root;
    }

    ObjectNode asDict() {
        return (ObjectNode) root;
    }

    ArrayNode asArray() {
        return (ArrayNode) root;
    }

    public Map<String, Object> toDataMap() {
        if (!(root instanceof ObjectNode)) {
            throw new IllegalStateException("Template is not a dict");
        }
        return ((ObjectNode) root).toData();
    }

    public List<Object> toDataList() {
        if (!(root instanceof ArrayNode)) {
            throw new IllegalStateException("Template is not a dict");
        }
        return ((ArrayNode) root).toData();
    }

    public Object toData() {
        return ((DataNode<?>) root).toData();
    }
}

