package tools.mermaid.generator.functions;

import tools.mermaid.generator.data.*;

import java.io.PrintWriter;

public final class ToMarkdown {
    private ToMarkdown() {
    }

    public static void convert(Mermaid mermaid, PrintWriter printWriter) {
        printWriter.println("```mermaid");
        convert(mermaid.diagram(), printWriter);
        printWriter.println("```");
        printWriter.println();

    }

    private static void convert(Diagram diagram, PrintWriter printWriter) {
       if (diagram instanceof ClassDiagram classDiagram) {
           printWriter.println("classDiagram");
           convert(classDiagram.direction(), printWriter);
           for (final var namespace : classDiagram.namespaces()) {
               convert(namespace, printWriter);
           }
           for(final var relation: classDiagram.relations()) {
               convert(relation, printWriter);
           }
       }
    }

    private static void convert(Relation relation, PrintWriter printWriter) {
        if (relation instanceof Realize realize) {
            printWriter.print(realize.anInterface().name());
            printWriter.print("<|..");
            printWriter.println(realize.clazz().name());
        }
        if (relation instanceof Extend extend) {
            printWriter.print(extend.parent().name());
            printWriter.print("<|--");
            printWriter.println(extend.child().name());
        }
        if (relation instanceof Use use) {
            printWriter.print(use.user().name());
            printWriter.print("..>");
            printWriter.print(use.used().name());
            printWriter.println(": use");
        }
        if (relation instanceof Create create) {
            printWriter.print(create.factory().name());
            printWriter.print("..>");
            printWriter.print(create.created().name());
            printWriter.println(": create");
        }
        if (relation instanceof Associate associate) {
            printWriter.print(associate.typeA().name());
            printWriter.print("-->");
            printWriter.println(associate.typeB().name());
        }
    }


    private static void convert(Namespace namespace, PrintWriter printWriter) {
        printWriter.print("namespace ");
        printWriter.print(namespace.name());
        printWriter.println(" {");
        for (final var type : namespace.types()) {
            convert(type, printWriter);
        }
        printWriter.println("}");
    }

    private static void convert(Type type, PrintWriter printWriter) {
        if (type instanceof Clazz aClass) {
            printWriter.print("   class ");
            printWriter.println(aClass.name());
        }
        if (type instanceof Interface anInterface) {
            printWriter.print("   class ");
            printWriter.print(anInterface.name());
            printWriter.println(" {");
            printWriter.println("      <<interface>>");
            printWriter.println("   }");
        }
    }

    private static void convert(Direction direction, PrintWriter printWriter) {
        printWriter.print("direction ");
        switch (direction) {
            case LEFT_RIGHT -> printWriter.println("LR");
            case TOP_DOWN -> printWriter.println("TD");
        }
    }
}
