package ru.job4j.todo.filter;

public enum FilterOptions {

    ALL("All"), NEW("New"), DONE("Done");

    private String value;

    FilterOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
