package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao)
    {
        this.categoryDao = categoryDao;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Category> getAll()
    {
        try
        {
            return categoryDao.getAllCategories();
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        try
        {
            Category category = categoryDao.getById(id);
            if(category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return category;
        }
        catch(ResponseStatusException notFound)
        {
            throw notFound;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        try
        {
            Category created = categoryDao.create(category);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        try
        {
            Category existing = categoryDao.getById(id);
            if(existing == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            category.setCategoryId(id);
            categoryDao.update(category);
        }
        catch(ResponseStatusException notFound)
        {
            throw notFound;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        try
        {
            Category category = categoryDao.getById(id);
            if(category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            categoryDao.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(ResponseStatusException notFound)
        {
            throw notFound;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}