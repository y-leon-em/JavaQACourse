import animals.Animal;
import animals.AnimalType;
import animals.flying.Duck;
import animals.land.Cat;
import animals.land.Dog;
import service.Menu;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Animal> animals = new ArrayList<>();

        System.out.println("Добро пожаловать!");

        while (true) {
            Menu command = readMenuCommand(scanner);
            switch (command) {
                case ADD:
                    addAnimal(scanner, animals);
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

    private static Menu readMenuCommand(Scanner scanner) {
        while (true) {
            System.out.print("\nВведите команду (add/list/exit): ");
            String input = scanner.nextLine();
            Menu command = Menu.fromString(input);
            if (command != null) {
                return command;
            }
            System.out.println("Неизвестная команда. Допустимые: add, list, exit.");
        }
    }

    private static void addAnimal(Scanner scanner, ArrayList<Animal> animals) {
        AnimalType type = readAnimalType(scanner);
        String name = readName(scanner);
        int age = readAge(scanner);
        double weight = readWeight(scanner);
        String color = readColor(scanner);

        Animal animal = createAnimal(type, name, age, weight, color);
        animals.add(animal);
        System.out.println("Животное добавлено!");
        animal.say();
    }

    private static AnimalType readAnimalType(Scanner scanner) {
        while (true) {
            System.out.print("Какое животное (cat/dog/duck)? ");
            AnimalType type = AnimalType.fromString(scanner.nextLine());
            if (type != null) {
                return type;
            }
            System.out.println("Неверный тип животного. Допустимые значения: cat, dog, duck.");
        }
    }

    private static String readName(Scanner scanner) {
        while (true) {
            System.out.print("Имя: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("Имя не может быть пустым. Попробуйте снова.");
        }
    }

    private static int readAge(Scanner scanner) {
        while (true) {
            System.out.print("Возраст (целое неотрицательное число): ");
            String input = scanner.nextLine().trim();
            try {
                int age = Integer.parseInt(input);
                if (age >= 0) {
                    return age;
                }
                System.out.println("Возраст не может быть отрицательным.");
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть целым числом.");
            }
        }
    }

    private static double readWeight(Scanner scanner) {
        while (true) {
            System.out.print("Вес (положительное число): ");
            String input = scanner.nextLine().trim();
            try {
                double weight = Double.parseDouble(input);
                if (weight > 0) {
                    return weight;
                }
                System.out.println("Вес должен быть больше нуля.");
            } catch (NumberFormatException e) {
                System.out.println("Вес должен быть числом.");
            }
        }
    }

    private static String readColor(Scanner scanner) {
        System.out.print("Цвет: ");
        return scanner.nextLine().trim();
    }

    private static Animal createAnimal(AnimalType type,
                                       String name,
                                       int age,
                                       double weight,
                                       String color) {
        switch (type) {
            case CAT:
                return new Cat(name, age, weight, color);
            case DOG:
                return new Dog(name, age, weight, color);
            case DUCK:
                return new Duck(name, age, weight, color);
            default:
                throw new IllegalStateException("Неизвестный тип: " + type);
        }
    }

    private static void listAnimals(ArrayList<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        System.out.println("Список животных:");
        for (Animal a : animals) {
            System.out.println(a);
        }
    }
}