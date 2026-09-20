package animals.land;

import animals.Animal;
import animals.properties.Color;


public class Cat extends Animal {


    public Cat(String name, int age, double weight, String color) {
        super();
        setName(name);
        setAge(age);
        setWeight(weight);
        setColor(Color.fromString(color));
    }

    @Override
    public void say() {
        System.out.println("Мяу");
    }

}
