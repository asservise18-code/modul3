package com.example.modul3.service;

import com.example.modul3.dto.AddProductToOrderRequestDto;
import com.example.modul3.dto.OrderItemResponseDto;
import com.example.modul3.dto.UpdateOrderItemQuantityRequestDto;
import com.example.modul3.exception.InvalidQuantityException;
import com.example.modul3.exception.OrderIdException;
import com.example.modul3.exception.OrderItemIdException;
import com.example.modul3.exception.ProductIdException;
import com.example.modul3.mapper.OrderItemMapper;
import com.example.modul3.model.Order;
import com.example.modul3.model.OrderItem;
import com.example.modul3.model.Product;
import com.example.modul3.repository.OrderItemRepository;
import com.example.modul3.repository.OrderRepository;
import com.example.modul3.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    public OrderItemResponseDto addProductToOrder(Long orderId, AddProductToOrderRequestDto dto) {
        if (dto == null || dto.getQuantity() == null || dto.getQuantity() < 1) {
            log.warn("Некорректное количество для заказа с id={}", orderId);
            throw new InvalidQuantityException();
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdException(orderId));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductIdException(dto.getProductId()));

        OrderItem orderItem = orderItemRepository.findByOrderAndProduct(order, product)
                .orElse(null);

        if (orderItem != null) {
            log.info("Товар уже есть в заказе, увеличиваем количество: orderId={}, productId={}, oldQuantity={}, addedQuantity={}",
                    orderId, product.getId(), orderItem.getQuantity(), dto.getQuantity());

            orderItem.setQuantity(orderItem.getQuantity() + dto.getQuantity());
        } else {
            orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
        }

        OrderItem saved = orderItemRepository.save(orderItem);

        return orderItemMapper.toDto(saved);
    }

    @Transactional
    public OrderItemResponseDto updateQuantity(Long orderItemId, UpdateOrderItemQuantityRequestDto dto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemIdException(orderItemId));

        orderItem.setQuantity(dto.getQuantity());

        OrderItem updated = orderItemRepository.save(orderItem);

        return orderItemMapper.toDto(updated);
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

        return orderItemMapper.toDtoList(orderItems);
    }
}