package com.example.modul3.service;

import com.example.modul3.dto.AddProductToOrderRequestDto;
import com.example.modul3.dto.OrderItemResponseDto;
import com.example.modul3.dto.UpdateOrderItemQuantityRequestDto;
import com.example.modul3.exception.OrderIdException;
import com.example.modul3.exception.OrderItemIdException;
import com.example.modul3.exception.ProductIdException;
import com.example.modul3.model.Order;
import com.example.modul3.model.OrderItem;
import com.example.modul3.model.Product;
import com.example.modul3.repository.OrderItemRepository;
import com.example.modul3.repository.OrderRepository;
import com.example.modul3.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderItemResponseDto addProductToOrder(Long orderId, AddProductToOrderRequestDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdException(orderId));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductIdException(dto.getProductId()));

        OrderItem orderItem = orderItemRepository.findByOrderAndProduct(order, product)
                .orElse(null);

        if (orderItem != null) {
            orderItem.setQuantity(orderItem.getQuantity() + dto.getQuantity());
        } else {
            orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
        }
        OrderItem saved = orderItemRepository.save(orderItem);

        return new OrderItemResponseDto(
                saved.getId(),
                saved.getOrder().getId(),
                saved.getProduct().getId(),
                saved.getProduct().getName(),
                saved.getQuantity()
        );
    }

    @Transactional
    public OrderItemResponseDto updateQuantity(Long orderItemId, UpdateOrderItemQuantityRequestDto dto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemIdException(orderItemId));
        orderItem.setQuantity(dto.getQuantity());

        OrderItem updated = orderItemRepository.save(orderItem);

        return new OrderItemResponseDto(
                updated.getId(),
                updated.getOrder().getId(),
                updated.getProduct().getId(),
                updated.getProduct().getName(),
                updated.getQuantity()
        );
    }

    @Transactional
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemIdException(orderItemId));
        orderItemRepository.delete(orderItem);
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> getItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdException(orderId));
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        return orderItems.stream()
                .map(orderItem -> new OrderItemResponseDto(orderItem.getId(),
                        orderItem.getOrder().getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity()
                )).toList();
    }

}
