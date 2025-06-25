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
        int userId = userDao.getByUserName(principal.getName()).getId();
        return cartDao.getCartItems(userId);
    }

    @PostMapping("/products/{productId}")
    public void addToCart(Principal principal, @PathVariable int productId) {
        int userId = userDao.getByUserName(principal.getName()).getId();
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

