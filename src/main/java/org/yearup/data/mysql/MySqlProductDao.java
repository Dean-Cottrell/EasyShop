package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class MySqlProductDao extends MySqlDaoBase implements ProductDao {

    public MySqlProductDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Product> search(Integer categoryId,
                                BigDecimal minPrice,
                                BigDecimal maxPrice,
                                String color) {
        StringBuilder sql = new StringBuilder("""
            SELECT product_id,
                   name,
                   price,
                   category_id,
                   description,
                   color,
                   stock,
                   image_url,
                   featured
              FROM products
             WHERE 1 = 1
        """);
        List<Object> params = new ArrayList<>();

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }
        if (color != null && !color.isBlank()) {
            sql.append(" AND LOWER(color) = LOWER(?)");
            params.add(color.trim());
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            List<Product> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
            return results;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error executing search", e);
        }
    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {
        String sql = """
            SELECT product_id,
                   name,
                   price,
                   category_id,
                   description,
                   color,
                   stock,
                   image_url,
                   featured
              FROM products
             WHERE category_id = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);

            ResultSet rs = stmt.executeQuery();
            List<Product> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
            return results;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error listing by category", e);
        }
    }

    @Override
    public Product getById(int productId) {
        String sql = """
            SELECT product_id,
                   name,
                   price,
                   category_id,
                   description,
                   color,
                   stock,
                   image_url,
                   featured
              FROM products
             WHERE product_id = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error fetching product by ID", e);
        }
    }

    @Override
    public Product create(Product product) {
        String sql = """
            INSERT INTO products
                (name, price, category_id, description, color, image_url, stock, featured)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getCategoryId());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getColor());
            stmt.setString(6, product.getImageUrl());
            stmt.setInt(7, product.getStock());
            stmt.setBoolean(8, product.isFeatured());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return getById(keys.getInt(1));
                }
                else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = """
            UPDATE products
               SET name        = ?,
                   price       = ?,
                   category_id = ?,
                   description = ?,
                   color       = ?,
                   image_url   = ?,
                   stock       = ?,
                   featured    = ?
             WHERE product_id = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getCategoryId());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getColor());
            stmt.setString(6, product.getImageUrl());
            stmt.setInt(7, product.getStock());
            stmt.setBoolean(8, product.isFeatured());
            stmt.setInt(9, product.getProductId());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    @Override
    public List<Product> getByCategoryId(int categoryId) {
        return listByCategoryId(categoryId);
    }

    protected static Product mapRow(ResultSet row) throws SQLException {
        return new Product(
                row.getInt("product_id"),
                row.getString("name"),
                row.getBigDecimal("price"),
                row.getInt("category_id"),
                row.getString("description"),
                row.getString("color"),
                row.getInt("stock"),
                row.getBoolean("featured"),
                row.getString("image_url")
        );
    }
}