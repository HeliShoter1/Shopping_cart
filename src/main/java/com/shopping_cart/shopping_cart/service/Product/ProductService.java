package com.shopping_cart.shopping_cart.service.Product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.shopping_cart.shopping_cart.dto.ImageDto;
import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.exceptions.AlreadyExistsException;
import com.shopping_cart.shopping_cart.exceptions.ProductNotFoundException;
import com.shopping_cart.shopping_cart.model.Category;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.reponsitory.CategoryRepository;
import com.shopping_cart.shopping_cart.reponsitory.ImageRepository;
import com.shopping_cart.shopping_cart.reponsitory.ProductRepository;
import com.shopping_cart.shopping_cart.request.AddProductRequets;
import com.shopping_cart.shopping_cart.request.ProductUpdateRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements  IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequets requets){

        if(productExist(requets.getName(),requets.getBrand())){
            throw new AlreadyExistsException("existing");
        }

        Category category = Optional.ofNullable(categoryRepository
        .findByName(requets.getCategory().getName()))
        .orElseGet(() ->{
            Category newCategory = new Category(requets.getCategory().getName());
            return categoryRepository.save(newCategory);
        });
        requets.setCategory(category);
        Product temp = productRepository.save(createProduct(requets, category));
        System.err.println(temp);
        return temp;
    }

    public Product createProduct(AddProductRequets requets, Category category){
        return new Product(requets.getName(),requets.getBrand(),requets.getPrice(),requets.getInventory(),requets.getDescription(),category);
    }

    public Product getProductById(Long Id){
        return productRepository.findById(Id).orElseThrow(()->new ProductNotFoundException("Product not Found"));
    }
    public void deleteProductById(Long Id){
        productRepository.findById(Id).ifPresentOrElse(productRepository::delete,()->{
            throw new ProductNotFoundException("Product not found");
        });
    }
    public Product updateProduct(ProductUpdateRequest request,Long Id){
        return productRepository.findById(Id)
            .map(existingProduct -> updateExistingProduct(existingProduct, request))
            .map(productRepository::save)
            .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }

    public Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setName(category.getName());
        return existingProduct;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getAllProductsByCategory(String category){
        return productRepository.findByCategoryName(category);
    }

    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    public List<Product> getAllProductsByBrand(String brand){
        return productRepository.findByBrand(brand);
    }

    public List<Product> getAllProductsByName(String name){
        return productRepository.findByName(name);
    }

    public List<Product> getAllProductsByNameAndBrand(String name,String brand){
        return productRepository.findByBrandAndName(brand, name);
    }

    public Long countProductsByBrandAndName(String name, String brand){
        return productRepository.countByBrandAndName(brand, name);
    }

    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertDto).toList();
    }

    public ProductDto convertDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images =  imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                                    .map(image -> modelMapper.map(image, ImageDto.class))
                                    .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    public boolean productExist(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }
}
