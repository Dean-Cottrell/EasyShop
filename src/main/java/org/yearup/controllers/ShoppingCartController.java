package org.yearup.controllers;

import org.springframework.web.bind.annotation.*;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCartItem;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartDao cartDao;
    private final UserDao userDao;

    public ShoppingCartController(ShoppingCartDao cartDao, UserDao userDao) {
        this.cartDao = cartDao;
        this.userDao = userDao;
    }

    @GetMapping
    public List<ShoppingCartItem> getCart(Principal principal) {
        int userId = userDao.getUserById(Integer.parseInt(principal.getName())).getUserId();
        return cartDao.getCartItems(userId);
    }

    @PostMapping("/products/{productId}")
    public void addToCart(Principal principal, @PathVariable int productId) {
        int userId = userDao.getUserById(principal.getName()).getUserId();
        List<ShoppingCartItem> items = cartDao.getCartItems(userId);
        boolean exists = items.stream()
                .anyMatch(i -> i.getProduct().getProductId() == productId);
        if (exists) {
            int currentQty = items.stream()
                    .filter(i -> i.getProduct().getProductId() == productId)
                    .findFirst()
                    .get()
                    .getQuantity();
            cartDao.updateCartItem(userId, productId, currentQty + 1);
        } else {
            cartDao.insertCartItem(userId, productId, 1);
        }
    }

    @PutMapping("/products/{productId}")
    public void updateCartItem(Principal principal, @PathVariable int productId, @RequestBody int quantity) {
        int userId = userDao.getByUserName(principal.getName()).getUserById();
        cartDao.updateCartItem(userId, productId, quantity);
    }

    @DeleteMapping("/products/{productId}")
    public void removeItem(Principal principal, @PathVariable int productId) {
        int userId = userDao.getByUserName(principal.getName()).getUserById();
        cartDao.deleteCartItem(userId, productId);
    }

    @DeleteMapping
    public void clearCart(Principal principal) {
        int userId = userDao.getByUserName(principal.getName()).getUserById();
        cartDao.clearCart(userId);
    }
}