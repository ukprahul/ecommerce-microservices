package com.ecommerce.ProductService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.ProductService", "com.ecommerce.search"})
@EnableElasticsearchRepositories(basePackages = "com.ecommerce.search")
public class ProductServiceEcomm {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceEcomm.class, args);
    }

}
