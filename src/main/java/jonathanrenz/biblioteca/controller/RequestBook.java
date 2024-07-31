package jonathanrenz.biblioteca.controller;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

public record RequestBook(
        @NotNull
        String name,
        @NotNull
        String type,
        @NotNull
        String entryDate) {
}