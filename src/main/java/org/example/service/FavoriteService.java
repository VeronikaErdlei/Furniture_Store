package org.example.service;

import org.example.entity.Favorite;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service



public class FavoriteService {

    private final FavoriteRepository favoriteRepository;


    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void addFavorite(User user, Product product) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }

    public List<Product> getFavorites(User user) {
        return favoriteRepository.findByUser(user).stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());
    }

    public void removeFavorite(User user, Product product) {
        Favorite favorite = favoriteRepository.findByUser(user).stream()
                .filter(f -> f.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

}