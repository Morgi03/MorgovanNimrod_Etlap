package hu.petrik.etlapprojekt.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapDB {

    Connection conn;
    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_DBNAME = "etlapdb";
    public static String DB_USER = "root";
    public static String DB_PASS = "";

    public EtlapDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DBNAME);
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public List<Etel> readFood() throws SQLException {
        List<Etel> foods = new ArrayList<>();
        String sql = "SELECT * FROM etlap";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("nev");
            String category = result.getString("kategoria");
            int price = result.getInt("ar");
            String desc = result.getString("leiras");
            Etel food = new Etel(id, name, category, price, desc);
            foods.add(food);
        }
        return foods;
    }

    public boolean createFood(Etel food) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, food.getName());
        stmt.setString(2, food.getDesc());
        stmt.setInt(3, food.getPrice());
        stmt.setString(4, food.getCategory());
        return stmt.executeUpdate() > 0;
    }

    public boolean deleteFood(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;
    }

    public boolean updateFood(Etel food) throws SQLException {
        String sql = "UPDATE etlap " +
                "SET nev = ?, " +
                "leiras = ?, " +
                "ar = ?, " +
                "kategoria = ?" +
                "WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, food.getName());
        stmt.setString(2, food.getDesc());
        stmt.setInt(3, food.getPrice());
        stmt.setString(4, food.getCategory());
        stmt.setInt(5, food.getId());
        return stmt.executeUpdate() > 0;
    }

    public boolean updateAllFoodPrice(int changeVal, boolean szazalek) throws SQLException {
        if (!szazalek) {
            String sql = "UPDATE etlap " +
                    "SET ar = ar + ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, changeVal);
            return stmt.executeUpdate() > 0;
        } else {
            String sql = "UPDATE etlap " +
                    "SET ar = ar * (1 + (? /100))";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, changeVal);
            return stmt.executeUpdate() > 0;
        }

    }
}
