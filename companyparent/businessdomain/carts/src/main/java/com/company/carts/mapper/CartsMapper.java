
package com.company.carts.mapper;



import com.company.carts.dto.CartProductDTO;
import com.company.carts.dto.CartsRequestDTO;
import com.company.carts.dto.CartsResponseDTO;
import com.company.carts.dto.external.FakeStoreCartProductDto;
import com.company.carts.dto.external.FakeStoreCartsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ruiz_
 */
@Component
public class CartsMapper {

   public CartsResponseDTO fakeStoreToCartResponse(FakeStoreCartsDto fakeStoreDto) {
        if (fakeStoreDto == null) {
            return null;
        }

        CartsResponseDTO response = new CartsResponseDTO();
        response.setId(fakeStoreDto.getId());
        response.setUserId(fakeStoreDto.getUserId());
        response.setDate(fakeStoreDto.getDate());
        
        // Mapeo de productos
        if (fakeStoreDto.getProducts() != null) {
            List<CartProductDTO> productDTOs = fakeStoreDto.getProducts().stream()
                .map(this::mapFakeStoreProductToDto)
                .collect(Collectors.toList());
            response.setProducts(productDTOs);
        }
        
        response.setVersion(fakeStoreDto.get__v());
        
        return response;
    }

    public FakeStoreCartsDto cartRequestToFakeStore(CartsRequestDTO request) {
        if (request == null) {
            return null;
        }

        FakeStoreCartsDto fakeStoreDto = new FakeStoreCartsDto();
        fakeStoreDto.setUserId(request.getUserId());
        fakeStoreDto.setDate(request.getDate());
        
        // Mapeo de productos
        if (request.getProducts() != null) {
            List<FakeStoreCartProductDto> fakeStoreProducts = request.getProducts().stream()
                .map(this::mapProductToFakeStore)
                .collect(Collectors.toList());
            fakeStoreDto.setProducts(fakeStoreProducts);
        }
        
        return fakeStoreDto;
    }

    private CartProductDTO mapFakeStoreProductToDto(FakeStoreCartProductDto fakeStoreProduct) {
        CartProductDTO productDTO = new CartProductDTO();
        productDTO.setProductId(fakeStoreProduct.getProductId());
        productDTO.setQuantity(fakeStoreProduct.getQuantity());
        return productDTO;
    }

    private FakeStoreCartProductDto mapProductToFakeStore(CartProductDTO productDTO) {
        FakeStoreCartProductDto fakeStoreProduct = new FakeStoreCartProductDto();
        fakeStoreProduct.setProductId(productDTO.getProductId());
        fakeStoreProduct.setQuantity(productDTO.getQuantity());
        return fakeStoreProduct;
    }

    // Método adicional para mapear lista de carritos
    public List<CartsResponseDTO> fakeStoreListToCartResponseList(List<FakeStoreCartsDto> fakeStoreDtos) {
        return fakeStoreDtos.stream()
            .map(this::fakeStoreToCartResponse)
            .collect(Collectors.toList());
    }
}