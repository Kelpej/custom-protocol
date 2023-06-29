package ukma.edu.ua.service;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.entity.Group;
import ukma.edu.ua.model.entity.Product;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.persistent.impl.ProductDao;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import java.util.List;
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

    public List<Product> getAll() {
        return productDao.getAll();
    }

    public Product updateProduct(Product updated) {
        productDao.update(updated);
        return updated;
    }

    public Product getById(long productId) throws NoEntityFoundException {
        Optional<Product> byId = productDao.findById(productId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(productId);
        }

        return byId.get();
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

    public void saveProduct(Product product) {
        productDao.save(product);
    }

    public void deleteProduct(long productId) throws NoEntityFoundException {
        Optional<Product> byId = productDao.findById(productId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(productId);
        }

        productDao.delete(byId.get());
    }
}
