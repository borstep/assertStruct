package org.assertstruct.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.template.node.ArrayNode;
import org.assertstruct.template.node.ObjectNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Template {
    TemplateNode root;

    public Template(TemplateNode root) {
        this.root = root;
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

