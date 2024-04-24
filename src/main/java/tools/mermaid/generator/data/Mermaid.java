package tools.mermaid.generator.data;

public record Mermaid(Diagram diagram) {
    public Mermaid() {
        this(new NoDiagram());
    }
}
