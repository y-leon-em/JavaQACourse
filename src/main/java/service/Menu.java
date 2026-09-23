package service;

public enum Menu {
    ADD,
    UPDATE,
    LIST,
    EXIT;

    public static Menu fromString(String input) {
        if (input == null) return null;
        try {
            return Menu.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}