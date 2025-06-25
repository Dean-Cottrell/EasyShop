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
}