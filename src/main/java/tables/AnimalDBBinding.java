package tables;

import animals.Animal;
import animals.properties.Color;
import db.SQLConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnimalDBBinding {

    private final SQLConnectionManager cm;

    public AnimalDBBinding(SQLConnectionManager cm) {
        this.cm = cm;
    }

    public void save(Animal animal) {
        String sql = "INSERT INTO animals (name, age, weight, color, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setString(1, animal.getName());
            ps.setInt(2, animal.getAge());
            ps.setDouble(3, animal.getWeight());
            ps.setString(4, animal.getColor() != null ? animal.getColor().name() : null);
            ps.setString(5, animal.getClass().getSimpleName().toUpperCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Animal> findAll() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals ORDER BY id ASC";
        try (PreparedStatement ps = cm.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                animals.add(mapRow(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return animals;
    }

    public void update(Animal animal) {
        String sql = "UPDATE animals SET name = ?, age = ?, weight = ?, color = ?, type = ? WHERE id = ?";
        try (PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setString(1, animal.getName());
            ps.setInt(2, animal.getAge());
            ps.setDouble(3, animal.getWeight());
            ps.setString(4, animal.getColor() != null ? animal.getColor().name() : null);
            ps.setString(5, animal.getClass().getSimpleName().toUpperCase());
            ps.setInt(6, animal.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM animals WHERE id = ?";
        try (PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Animal> findByType(String type) {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals WHERE type = ? ORDER BY id ASC";
        try (PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setString(1, type.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    animals.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return animals;
    }

    public Animal findById(int id) {
        String sql = "SELECT * FROM animals WHERE id = ?";
        try (PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Animal mapRow(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Animal animal;
        switch (type == null ? "" : type.toUpperCase()) {
            case "CAT":
                animal = new animals.land.Cat();
                break;
            case "DOG":
                animal = new animals.land.Dog();
                break;
            case "DUCK":
                animal = new animals.flying.Duck();
                break;
            default:
                throw new SQLException("Неизвестный тип животного в БД: " + type);
        }
        fillAnimalFromResultSet(animal, rs);
        return animal;
    }

    private void fillAnimalFromResultSet(Animal animal, ResultSet rs) throws SQLException {
        animal.setName(rs.getString("name"));
        animal.setAge(rs.getInt("age"));
        animal.setWeight(rs.getDouble("weight"));
        String colorStr = rs.getString("color");
        if (colorStr != null) {
            try {
                animal.setColor(Color.valueOf(colorStr));
            } catch (IllegalArgumentException e) {
                animal.setColor(Color.UNDEFINED);
            }
        }
        animal.setId(rs.getInt("id"));
    }
}