package tools.mermaid.generator.data;

public sealed interface Type permits Clazz, Interface {
    String name();
}
