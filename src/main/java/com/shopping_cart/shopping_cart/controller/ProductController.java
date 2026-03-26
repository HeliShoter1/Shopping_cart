package com.shopping_cart.shopping_cart.controller;

import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_cart.shopping_cart.annotation.RateLimit;
import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.request.AddProductRequets;
import com.shopping_cart.shopping_cart.request.ProductUpdateRequest;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.Product.IProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @RateLimit(requests = 5, seconds = 10)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(
        @RequestParam Long cursor,
        @RequestParam Long limit
    ){
        try {
            List<Product> products = productService.getAllProducts(cursor,limit);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", convertedProduct));
        } catch (Exception e) {
            // TODO: handle exception
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
        
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id
                                                    ,HttpServletRequest request
                                                ){
        try {
            Product product = productService.getProductById(id);
            String etag = "\"" + product.getUpdateAt().toEpochSecond(ZoneOffset.UTC) + "\""; 
            String ifNoneMatch = request.getHeader("If-None-Match");
            if(etag.equals(ifNoneMatch)){
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            ProductDto productDto = productService.convertDto(product);
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES)
                        .cachePublic()
                        .sMaxAge(1, TimeUnit.HOURS))
                    .header(HttpHeaders.ETAG, etag)
                    .body(new ApiResponse("Success", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequets product){
        try {
            Product addProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertDto(addProduct);
            return ResponseEntity.ok(new ApiResponse("Found", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest productUpdate, @PathVariable("productId") Long productId){
        try {
            Product product = productService.updateProduct(productUpdate, productId);
            ProductDto productDto = productService.convertDto(product);
            return ResponseEntity.ok(new ApiResponse("Update success", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam("brand") String brand,@RequestParam("name") String name){
        try {
            List<Product> products = productService.getAllProductsByNameAndBrand(name, brand);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", convertedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/products/by/brand-and-category")
    public ResponseEntity<ApiResponse> getProductByBrandAndCategory(@RequestParam("brand") String brand,@RequestParam("category") String category){
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", convertedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    
    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable("name") String name){
        try {
            List<Product> products = productService.getAllProductsByName(name);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", convertedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam("brand") String brand){
        try {
            List<Product> products = productService.getAllProductsByBrand(brand);
            return ResponseEntity.ok(new ApiResponse("Found", products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/products/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable("category") String category){
        try {
            List<Product> products = productService.getAllProductsByCategory(category);
            return ResponseEntity.ok(new ApiResponse("Found", products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(name, brand);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}


