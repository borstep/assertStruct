package ua.kiev.its.assertstruct.impl.converter.jackson;

import lombok.Getter;
import ua.kiev.its.assertstruct.converter.JsonStruct;
import ua.kiev.its.assertstruct.utils.Markers;

public class RootStruct implements JsonStruct {
    @Getter
    private Object value = Markers.EOS;

    @Override
    public void addChild(String key, Object value) {
        if (this.value != Markers.EOS) {
            throw new IllegalStateException("Can't add second child to root");
        }
        this.value = value;
    }
}
