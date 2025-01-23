package kr.hhplus.be.server.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.dto.PageResponse;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductRepository;
import kr.hhplus.be.server.product.domain.ProductResult;
import kr.hhplus.be.server.stock.domain.Stock;
import kr.hhplus.be.server.stock.domain.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("상품 조회시 id, name, price, stock 을 포함한 전체 상품 목록이 조회된다")
    public void test() throws Exception {
        // given
        Product apple = Product.builder()
                .name("apple")
                .price(10000L)
                .build();
        productRepository.save(apple);

        Product mango = Product.builder()
                .name("mango")
                .price(20000L)
                .build();
        productRepository.save(mango);

        Stock appleStock = Stock.builder()
                .productId(apple.getId())
                .quantity(10)
                .build();
        stockRepository.save(appleStock);

        Stock mangoStock = Stock.builder()
                .productId(mango.getId())
                .quantity(20)
                .build();
        stockRepository.save(mangoStock);

        MvcResult result = mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        PageResponse<ProductResult> response = objectMapper.readValue(responseBody, new TypeReference<PageResponse<ProductResult>>() {
        });

        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(10);

        // 개별 상품 데이터 검증
        ProductResult appleResult = response.getContent().stream()
                .filter(product -> product.getName().equals("apple"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("apple not found"));

        assertThat(appleResult.getPrice()).isEqualTo(10000L);
        assertThat(appleResult.getStock()).isEqualTo(10);

        ProductResult mangoResult = response.getContent().stream()
                .filter(product -> product.getName().equals("mango"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("mango not found"));

        assertThat(mangoResult.getPrice()).isEqualTo(20000L);
        assertThat(mangoResult.getStock()).isEqualTo(20);
    }
}
