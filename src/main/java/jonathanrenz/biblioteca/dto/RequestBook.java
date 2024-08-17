package jonathanrenz.biblioteca.dto;

import org.antlr.v4.runtime.misc.NotNull;

public record RequestBook(
        @NotNull
        String name,
        @NotNull
        String type,
        @NotNull
        String entryDate) {
}