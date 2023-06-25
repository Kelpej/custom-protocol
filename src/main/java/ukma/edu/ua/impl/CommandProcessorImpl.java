package ukma.edu.ua.impl;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.CommandProcessor;
import ukma.edu.ua.model.command.*;
import ukma.edu.ua.service.GroupService;
import ukma.edu.ua.service.ProductService;

@RequiredArgsConstructor
public class CommandProcessorImpl implements CommandProcessor {
    private final ProductService productService;
    private final GroupService groupService;

    @Override
    public Object apply(Command command) {
        return switch (command) {
            case IncreaseProductQuantityCommand com -> productService.increaseQuantity(com.id(), com.quantity());
            case DecreaseProductQuantityCommand com -> productService.decreaseQuantity(com.id(), com.quantity());
            case AddGroupCommand com -> groupService.addGroup(com.name(), com.description());
            case AddGroupMemberCommand com -> productService.addProductToGroup(com.productId(), com.groupId());
            case GetProductQuantityCommand com -> productService.getProductQuantity(com.id());
            case SetProductPriceCommand com -> productService.setProductPrice(com.id(), com.price());
            default -> throw new IllegalStateException("Unexpected command: " + command);
        };
    }
}
