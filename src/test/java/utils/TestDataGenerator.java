package utils;

import net.datafaker.Faker;

public final class TestDataGenerator {

    private static final Faker faker = new Faker();

    private TestDataGenerator() {}

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    public static String randomPassword() {
        return faker.internet().password();
    }

    public static String randomWishlistTitle() {
        return faker.commerce().productName();
    }

    public static String randomWishlistDescription() {
        return faker.lorem().sentence(5);
    }
}