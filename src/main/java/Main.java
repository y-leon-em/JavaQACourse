import animals.Animal;
import animals.AnimalType;
import animals.flying.Duck;
import animals.land.Cat;
import animals.land.Dog;
import db.SQLConnectionManager;
import service.Menu;
import tables.AnimalTable;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SQLConnectionManager connectionManager = new SQLConnectionManager();
        AnimalTable animalTable = new AnimalTable(connectionManager);

        try {
            animalTable.create();
        } catch (Exception e) {
            System.out.println("Не удалось подготовить таблицу животных: " + e.getMessage());
            scanner.close();
            return;
        }

        System.out.println("Добро пожаловать!");

        while (true) {
            Menu command = readMenuCommand(scanner);
            try {
                switch (command) {
                    case ADD:
                        addAnimal(scanner, animalTable);
                        break;
                    case LIST:
                        listAnimals(animalTable.list());
                        break;
                    case FILTER:
                        filterAnimals(scanner, animalTable);
                        break;
                    case UPDATE:
                        updateAnimal(scanner, animalTable);
                        break;
                    case DELETE:
                        deleteAnimal(scanner, animalTable);
                        break;
                    case EXIT:
                        System.out.println("Выход из программы.");
                        scanner.close();
                        return;
                }
            } catch (Exception e) {
                System.out.println("Ошибка при работе с БД: " + e.getMessage());
            }
        }
    }

    private static Menu readMenuCommand(Scanner scanner) {
        while (true) {
            System.out.print("\nВведите команду (add/list/filter/update/delete/exit): ");
            String input = scanner.nextLine();
            Menu command = Menu.fromString(input);
            if (command != null) {
                return command;
            }
            System.out.println("Неизвестная команда. Допустимые: add, list, filter, update, delete, exit.");
        }
    }

    private static void addAnimal(Scanner scanner, AnimalTable table) throws Exception {
        AnimalType type = readAnimalType(scanner);
        String name = readName(scanner);
        int age = readAge(scanner);
        double weight = readWeight(scanner);
        String color = readColor(scanner);

        Animal animal = createAnimal(type, name, age, weight, color);
        table.save(animal);
        System.out.println("Животное добавлено!");
        animal.say();
    }

    private static void filterAnimals(Scanner scanner, AnimalTable table) throws Exception {
        AnimalType type = readAnimalType(scanner);
        List<Animal> animals = table.findByType(type.name());
        if (animals.isEmpty()) {
            System.out.println("Животные типа " + type + " не найдены.");
            return;
        }
        System.out.println("Животные типа " + type + ":");
        for (Animal a : animals) {
            System.out.println(a);
        }
    }

    private static void updateAnimal(Scanner scanner, AnimalTable table) throws Exception {
        listAnimals(table.list());
        int id = readId(scanner, table);
        AnimalType type = readAnimalType(scanner);

        Animal updated = createAnimal(type,
                readName(scanner),
                readAge(scanner),
                readWeight(scanner),
                readColor(scanner));
        updated.setId(id);
        table.update(updated);
        System.out.println("Животное с id=" + id + " обновлено!");
    }

    private static void deleteAnimal(Scanner scanner, AnimalTable table) throws Exception {
        int id = readPositiveInt(scanner, "Введите id животного для удаления: ");
        table.delete(id);
        System.out.println("Животное с id=" + id + " удалено.");
    }

    private static int readId(Scanner scanner, AnimalTable table) throws Exception {
        while (true) {
            int id = readPositiveInt(scanner, "Введите id животного для редактирования: ");
            Animal existing = table.findById(id);
            if (existing != null) {
                return id;
            }
            System.out.println("Животное с id=" + id + " не найдено. Попробуйте снова.");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Значение должно быть положительным целым числом.");
            } catch (NumberFormatException e) {
                System.out.println("Значение должно быть целым числом.");
            }
        }
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

    private static void listAnimals(List<Animal> animals) {
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