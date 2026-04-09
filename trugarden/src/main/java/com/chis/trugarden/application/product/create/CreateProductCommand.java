package com.chis.trugarden.application.product.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateProductCommand {
    private String name;
    private String slug;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal unitPrice;
    private String category;
    private int stock;
    private boolean hasIva;
    private int ivaPercentage;
    private List<MultipartFile> images;
}
