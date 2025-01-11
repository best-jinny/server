package kr.hhplus.be.server.product;


import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductTest {
    @Test
    @DisplayName("상품 가격이 일치하지 않으면 InvalidException 이 발생한다")
    void validatePrice_whenGivenInvalidPrice_thenThrowInvalidException() {
        // given
        Product product = Product.builder()
                .price(50_000L)
                .build();
        // when & then
        assertThatThrownBy(() -> product.validatePrice(50_500L))
                .isInstanceOf(InvalidException.class);
    }
}
