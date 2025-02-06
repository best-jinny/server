package kr.hhplus.be.server.product.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductRepository;
import kr.hhplus.be.server.product.domain.ProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.product.domain.QProduct.product;
import static kr.hhplus.be.server.stock.domain.QStock.stock;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Page<ProductResult> findWithStock(Pageable pageable) {
        List<ProductResult> results =  queryFactory
                .select(Projections.constructor(
                        ProductResult.class,
                        product.id,
                        product.name,
                        product.price,
                        stock.quantity.coalesce(0)
                ))
                .from(product)
                .leftJoin(stock).on(product.id.eq(stock.productId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                .select(product.count())
                .from(product)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public void deleteAll() {
        productJpaRepository.deleteAllInBatch();
    }
}
