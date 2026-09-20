package animals.properties;

public enum Color {
    UNDEFINED("неизвестный"),
    RED("рыжий"),
    BROWN("коричневый"),
    BLACK("черный"),
    WHITE("белый"),
    YELLOW("желтый"),
    MULTICOLOR("разноцветный");


    public String getValue() {
        return value;
    }

    private final String value ;
    Color(String value) {
        this.value = value;
    }

    public static Color fromString(String text) {
        if (text == null) return UNDEFINED;
        try {
            return Color.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNDEFINED;
        }
    }

}
