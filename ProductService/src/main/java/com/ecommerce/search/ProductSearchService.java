package com.ecommerce.search;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public ProductSearchService(ProductSearchRepository productSearchRepository) {
        this.productSearchRepository = productSearchRepository;
    }

    public ProductDocument indexProduct(ProductDocument doc) {
        return productSearchRepository.save(doc);
    }

    public List<ProductDocument> searchProducts(String query) {
        return productSearchRepository.findByTitleContainingOrDescriptionContaining(query, query);
    }
}
