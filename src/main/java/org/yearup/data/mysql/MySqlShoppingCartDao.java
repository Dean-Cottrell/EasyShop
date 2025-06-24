package org.yearup.data;

import org.yearup.models.ShoppingCartItem;
import org.yearup.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao {
    private final JdbcTemplate jdbcTemplate;

    public MySqlShoppingCartDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ShoppingCartItem> rowMapper = new RowMapper<>() {
        @Override
        public ShoppingCartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product p = new Product();
            p.setProductId(rs.getInt("product_id"));
            p.setName(rs.getString("name"));
            p.setDescription(rs.getString("description"));
            p.setPrice(rs.getBigDecimal("price"));
            p.setCategoryId(rs.getInt("category_id"));
            p.setColor(rs.getString("color"));
            p.setImageUrl(rs.getString("image_url"));
            p.setStock(rs.getInt("stock"));
            p.setFeatured(rs.getBoolean("featured"));
            return new ShoppingCartItem(p, rs.getInt("quantity"));
        }
    };

    @Override
    public List<ShoppingCartItem> getCartItems(int userId) {
        String sql = "SELECT p.*, sc.quantity FROM products p JOIN shopping_cart sc ON p.product_id = sc.product_id WHERE sc.user_id = ?";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    @Override
    public void insertCartItem(int userId, int productId, int quantity) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, productId, quantity);
    }

    @Override
    public void updateCartItem(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, quantity, userId, productId);
    }

    @Override
    public void deleteCartItem(int userId, int productId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, userId, productId);
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}