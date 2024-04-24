package tools.mermaid.generator.functions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.mermaid.generator.data.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ToMarkdownTest {

    @Nested
    class generate_mermaid_code_block_to_markdown {
        @Test
        void with_no_diagram() {
            final var stringWriter = new StringWriter();
            final var mermaid = new Mermaid();

            ToMarkdown.convert(mermaid, new PrintWriter(stringWriter));

            assertThat(stringWriter.toString()).isEqualTo(toLines("""
                    ```mermaid
                    ```
                                        
                    """));
        }

        @Test
        void with_class_diagram_and_direction() {
            final var stringWriter = new StringWriter();
            final var mermaid = new Mermaid(new ClassDiagram(Direction.LEFT_RIGHT));

            ToMarkdown.convert(mermaid, new PrintWriter(stringWriter));

            assertThat(stringWriter.toString()).isEqualTo(toLines("""
                    ```mermaid
                    classDiagram
                    direction LR
                    ```
                                        
                    """));
        }

        @Test
        void with_class_diagram_and_namespace() {
            final var stringWriter = new StringWriter();
            final var mermaid = new Mermaid(new ClassDiagram(Direction.LEFT_RIGHT,
                    List.of(new Namespace("tools.mermaid.generator.functions"))));

            ToMarkdown.convert(mermaid, new PrintWriter(stringWriter));

            assertThat(stringWriter.toString()).isEqualTo(toLines("""
                    ```mermaid
                    classDiagram
                    direction LR
                    namespace tools_mermaid_generator_functions {
                    }
                    ```
                                        
                    """));
        }

        @Test
        void with_class_diagram_and_namespace_and_class() {
            final var stringWriter = new StringWriter();
            final var mermaid = new Mermaid(new ClassDiagram(Direction.LEFT_RIGHT,
                    List.of(new Namespace("tools.mermaid.generator.functions", List.of(new Clazz("ToMarkdown"))))));

            ToMarkdown.convert(mermaid, new PrintWriter(stringWriter));

            assertThat(stringWriter.toString()).isEqualTo(toLines("""
                    ```mermaid
                    classDiagram
                    direction LR
                    namespace tools_mermaid_generator_functions {
                       class ToMarkdown
                    }
                    ```
                                        
                    """));
        }

        @Test
        void with_class_diagram_and_namespaces_and_classes_and_interface() {
            final var stringWriter = new StringWriter();
            final var mermaid = new Mermaid(new ClassDiagram(Direction.LEFT_RIGHT,
                    List.of(
                            new Namespace("tools.mermaid.generator.functions",
                                    List.of(new Clazz("ToMarkdown"))),
                            new Namespace("tools.mermaid.generator.data",
                                    List.of(new Interface("Diagram"), new Clazz("Clazz")))

                    )));

            ToMarkdown.convert(mermaid, new PrintWriter(stringWriter));

            assertThat(stringWriter.toString()).isEqualTo(toLines("""
                    ```mermaid
                    classDiagram
                    direction LR
                    namespace tools_mermaid_generator_functions {
                       class ToMarkdown
                    }
                    namespace tools_mermaid_generator_data {
                       class Diagram {
                          <<interface>>
                       }
                       class Clazz
                    }
                    ```
                                        
                    """));
        }

        @Test
        void with_class_diagram_and_relations() {
            final var stringWriter = new StringWriter();
            final var classDiagram = new Clazz("ClassDiagram");
            final var diagram = new Interface("Diagram");
            final var vehicle = new Clazz("Vehicle");
            final var car = new Clazz("Car");
            final var vehicleFactory = new Clazz("VehicleFactory");
            final var toMarkdown = new Clazz("ToMarkdown");
            final var direction = new Clazz("Direction");

            final var mermaid = new Mermaid(new ClassDiagram(Direction.LEFT_RIGHT,
                    List.of(
                            new Namespace("tools.mermaid.generator.functions",
                                    List.of(toMarkdown)),
                            new Namespace("tools.mermaid.generator.data",
                                    List.of(diagram, classDiagram, direction)),
                            new Namespace("vehicle", List.of(car, vehicle, vehicleFactory))

                    ),
                    List.of(
                            new Realize(classDiagram, diagram),
                            new Extend(car, vehicle),
                            new Use(toMarkdown, classDiagram),
                            new Create(vehicleFactory, vehicle),
                            new Associate(classDiagram, direction)
                    )));

            ToMarkdown.convert(mermaid, new PrintWriter(stringWriter));

            assertThat(stringWriter.toString()).isEqualTo(toLines("""
                    ```mermaid
                    classDiagram
                    direction LR
                    namespace tools_mermaid_generator_functions {
                       class ToMarkdown
                    }
                    namespace tools_mermaid_generator_data {
                       class Diagram {
                          <<interface>>
                       }
                       class ClassDiagram
                       class Direction
                    }
                    namespace vehicle {
                       class Car
                       class Vehicle
                       class VehicleFactory
                    }
                    Diagram<|..ClassDiagram
                    Vehicle<|--Car
                    ToMarkdown..>ClassDiagram: use
                    VehicleFactory..>Vehicle: create
                    ClassDiagram-->Direction
                    ```
                                        
                    """));
            System.out.println(stringWriter);
        }
    }

    private static String toLines(String lines) {
        return lines.lines().map(l -> l + System.lineSeparator()).collect(Collectors.joining());
    }

}
