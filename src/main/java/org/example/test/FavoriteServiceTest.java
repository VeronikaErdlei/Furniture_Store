package org.example.test;

import org.example.entity.Favorite;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.repository.FavoriteRepository;
import org.example.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    public FavoriteServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFavorite() {

        Long userId = 1L;


        User user = new User();
        user.setId(userId);


        Product product = new Product();
        product.setId(1L);


        favoriteService.addFavorite(user, product);

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }


    @Test
    public void testGetFavorites() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(1L);

        Favorite favorite = new Favorite();
        favorite.setProduct(product);

        when(favoriteRepository.findByUser(user)).thenReturn(Collections.singletonList(favorite));

        List<Product> favorites = favoriteService.getFavorites(user);

        assertEquals(1, favorites.size());
        assertEquals(product, favorites.get(0));
    }


    @Test
    public void testRemoveFavorite() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(1L);

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        when(favoriteRepository.findByUser(user)).thenReturn(Collections.singletonList(favorite));

        favoriteService.removeFavorite(user, product);

        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    void addFavorite() {
    }

    @Test
    void getFavorites() {
    }

    @Test
    void removeFavorite() {
    }

    @Test
    void setFavoriteRepository() {
    }
}
