package ukma.edu.ua.service;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.entity.Group;
import ukma.edu.ua.model.entity.Product;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.persistent.impl.ProductDao;

import java.util.Optional;

@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;
    private final GroupDao groupDao;

    public Product increaseQuantity(long productId, int quantity) {
        Product byId = productDao.findById(productId).orElseThrow();

        Product updated = byId
                .toBuilder()
                .quantity(byId.quantity() + quantity)
                .build();

        productDao.save(updated);
        return updated;
    }

    public Product decreaseQuantity(long productId, int quantity) {
        Product byId = productDao.findById(productId).orElseThrow();

        Product updated = byId
                .toBuilder()
                .quantity(byId.quantity() - quantity)
                .build();

        productDao.save(updated);
        return updated;
    }

    public Product addProductToGroup(long productId, long groupId) {
        Optional<Group> group = groupDao.findById(groupId);

        if (group.isEmpty()) {
            throw new IllegalArgumentException("Group does not exist");
        }

        Product product = productDao.findById(productId).orElseThrow();
        Product updated = product.toBuilder().group(group.get()).build();

        productDao.save(updated);
        return updated;
    }

    public int getProductQuantity(long id) {
        return productDao.findById(id).orElseThrow().quantity();
    }

    public Product setProductPrice(long id, double price) {
        Product updated = productDao.findById(id).orElseThrow()
                .toBuilder()
                .price(price)
                .build();

        productDao.save(updated);
        return updated;
    }
}
