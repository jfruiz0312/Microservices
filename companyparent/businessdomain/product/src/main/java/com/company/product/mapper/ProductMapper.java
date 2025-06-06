package com.company.product.mapper;

import com.company.product.dto.ProductRequestDTO;
import com.company.product.dto.ProductResponseDTO;
import com.company.product.dto.external.FakeStoreProductDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 *
 * @author ruiz_
 */
@Component
public class ProductMapper {

    public ProductResponseDTO fakeStoreToProductResponse(FakeStoreProductDto fakeStoreDto) {
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(fakeStoreDto.getId());
        response.setTitle(fakeStoreDto.getTitle());
        response.setPrice(fakeStoreDto.getPrice().doubleValue());
        response.setDescription(fakeStoreDto.getDescription());
        response.setCategory(fakeStoreDto.getCategory());
        response.setImage(fakeStoreDto.getImage());
        return response;
    }

    public FakeStoreProductDto productRequestToFakeStore(ProductRequestDTO request) {
        FakeStoreProductDto fakeStoreDto = new FakeStoreProductDto();
        fakeStoreDto.setTitle(request.getTitle());
        fakeStoreDto.setPrice(BigDecimal.valueOf(request.getPrice()));
        fakeStoreDto.setDescription(request.getDescription());
        fakeStoreDto.setCategory(request.getCategory());
        fakeStoreDto.setImage(request.getImage());
        return fakeStoreDto;
    }
}
