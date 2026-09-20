package service;

public enum Menu {
    ADD,
    LIST,
    EXIT;

    public static Menu fromString(String input) {
        if (input == null) return null;
        String trimmed = input.trim().toUpperCase();
        try {
            return Menu.valueOf(trimmed);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}