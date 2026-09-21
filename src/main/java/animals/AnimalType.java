package animals;

public enum AnimalType {
    CAT, DOG, DUCK;
    public static AnimalType fromString(String input) {
        if (input == null) return null;
        String trimmed = input.trim().toUpperCase();
        try {
            return AnimalType.valueOf(trimmed);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
