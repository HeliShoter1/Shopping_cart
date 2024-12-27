package com.shopping_cart.shopping_cart.service.Product;

import java.util.List;

import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.request.AddProductRequets;
import com.shopping_cart.shopping_cart.request.ProductUpdateRequest;

public interface IProductService {
    Product addProduct(AddProductRequets product);
    Product getProductById(Long Id);
    void deleteProductById(Long Id);
    Product updateProduct(ProductUpdateRequest request,Long Id);    
    List<Product> getAllProducts();
    List<Product> getAllProductsByCategory(String category);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getAllProductsByBrand(String brand);
    List<Product> getAllProductsByName(String name);
    List<Product> getAllProductsByNameAndBrand(String name,String brand);
    Long countProductsByBrandAndName(String name, String brand);

    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto convertDto(Product product);

    boolean productExist(String name, String brand);
}
