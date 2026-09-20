import animals.Animal;
import animals.AnimalType;
import animals.flying.Duck;
import animals.land.Cat;
import animals.land.Dog;
import service.Menu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Animal> animals = new ArrayList<>();

        System.out.println("Добро пожаловать!");
        while (true) {
            System.out.print("\nВведите команду (add/list/exit): ");
            String input = scanner.nextLine();
            Menu command = Menu.fromString(input);

            if (command == null) {
                System.out.println("Неизвестная команда. Попробуйте снова.");
                continue;
            }

            switch (command) {
                case ADD:
                    try {
                        addAnimal(scanner, animals);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка ввода: " + e.getMessage());
                    }
                    break;
                case LIST:
                    listAnimals(animals);
                    break;
                case EXIT:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
            }
        }
    }

    // Метод для добавления животного
    private static void addAnimal(Scanner scanner, ArrayList<Animal> animals) {
        // 1. Тип животного
        System.out.print("Какое животное (cat/dog/duck)? ");
        String typeInput = scanner.nextLine().trim().toLowerCase();
        AnimalType type = AnimalType.fromString(typeInput);
        if (type == null) {
            throw new IllegalArgumentException("Неизвестный тип животного: " + typeInput);
        }

        // 2. Имя
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        // 3. Возраст
        System.out.print("Возраст (целое число): ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Возраст должен быть целым числом", e);
        }
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }

        // 4. Вес
        System.out.print("Вес (число): ");
        double weight;
        try {
            weight = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Вес должен быть числом", e);
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Вес не может быть отрицательным");
        }

        // 5. Цвет
        System.out.print("Цвет: ");
        String color = scanner.nextLine().trim();
        // (можно добавить проверку на пустоту)

        // 6. Создание животного
        Animal animal;
        switch (type) {
            case CAT:
                animal = new Cat(name, age, weight, color);
                break;
            case DOG:
                animal = new Dog(name, age, weight, color);
                break;
            case DUCK:
                animal = new Duck(name, age, weight, color);
                break;
            default:
                // этот случай не должен наступить, но на всякий случай
                throw new IllegalArgumentException("Неизвестный тип животного: " + type);
        }

        animals.add(animal);
        System.out.println("Животное добавлено!");
        animal.say();
    }

    // Метод для вывода списка животных
    private static void listAnimals(ArrayList<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            System.out.println("Список животных:");
            for (Animal a : animals) {
                System.out.println(a.toString());
            }
        }
    }
}