package org.yearup.data;

import org.yearup.models.ShoppingCartItem;
import java.util.List;

public interface ShoppingCartDao {
    List<ShoppingCartItem> getCartItems(int userId);
    void insertCartItem(int userId, int productId, int quantity);
    void updateCartItem(int userId, int productId, int quantity);
    void deleteCartItem(int userId, int productId);
    void clearCart(int userId);
}