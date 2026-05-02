package com.ecommerce.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private String id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String imageUrl;
}
