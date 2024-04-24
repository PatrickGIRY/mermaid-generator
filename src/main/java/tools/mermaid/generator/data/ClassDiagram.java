package tools.mermaid.generator.data;

import java.util.Collections;
import java.util.List;

public record ClassDiagram(Direction direction, List<Namespace> namespaces, List<Relation> relations) implements Diagram {
    public ClassDiagram {
        namespaces = List.copyOf(namespaces);
        relations = List.copyOf(relations);
    }

    public ClassDiagram(Direction direction) {
        this(direction, Collections.emptyList(), Collections.emptyList());
    }
    public ClassDiagram(Direction direction, List<Namespace> namespaces) {
        this(direction, namespaces, Collections.emptyList());
    }
}
