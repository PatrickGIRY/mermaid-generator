package tools.mermaid.generator.data;

import java.util.Collections;
import java.util.List;

public record Namespace(String name, List<Type> types) {
    public Namespace {
        name = name.replace('.', '_');
        types = List.copyOf(types);
    }

    public Namespace(String name) {
        this(name, Collections.emptyList());
    }
}
