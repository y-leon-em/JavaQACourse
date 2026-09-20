package animals.flying;

import animals.Animal;
import animals.properties.Color;

public class Duck extends Animal implements Flying {
    public Duck(String name, int age, double weight, String color) {
        super();
        setName(name);
        setAge(age);
        setWeight(weight);
        setColor(Color.fromString(color));
    }

    @Override
    public void fly() {
        System.out.println("Я лечу");
    }

    @Override
    public void say() {
        System.out.println("Кря");
    }
}
