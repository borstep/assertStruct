package data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder(builderMethodName = "pojo")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestPojo {
    private static AtomicInteger idx = new AtomicInteger(1);

    public static TestPojo nextPojo() {
        return new TestPojo(idx.getAndIncrement());
    }

    Integer num;

    @Singular(ignoreNullCollections = true)
    Map<String, TestPojo> children;

    @Singular(ignoreNullCollections = true)
    List<TestPojo> elements;

    @Singular
    @Getter(onMethod_ = @JsonAnyGetter)
    Map<String, String> others;

    ValueObject value;

    public TestPojo(Integer num, Map<String, TestPojo> children, List<TestPojo> elements, Map<String, String> others, ValueObject value) {
        this.num = num;
        this.children = children == null || children.isEmpty() ? null : children;
        this.elements = elements==null || elements.isEmpty() ? null : elements;
        this.others = others;
        this.value = value;
    }

    public TestPojo(int num) {
        this.num = num;
    }

}
