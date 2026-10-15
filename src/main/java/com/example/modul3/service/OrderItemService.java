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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    public OrderItemResponseDto addProductToOrder(Long orderId, AddProductToOrderRequestDto dto) {
        log.info("Добавление товара в заказ: id заказа={}, id товара={}, количество={}",
                orderId,
                dto != null ? dto.getProductId() : null,
                dto != null ? dto.getQuantity() : null);

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
            log.info("Товар уже есть в заказе, увеличиваем количество: id заказа={}, id товара={}, старое количество={}, добавленное количество={}",
                    orderId, product.getId(), orderItem.getQuantity(), dto.getQuantity());

            orderItem.setQuantity(orderItem.getQuantity() + dto.getQuantity());
        } else {
            log.info("Создание новой позиции заказа: id заказа={}, id товара={}, количество={}",
                    orderId, product.getId(), dto.getQuantity());

            orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
        }

        OrderItem saved = orderItemRepository.save(orderItem);

        log.info("Позиция заказа успешно сохранена, id позиции={}, id заказа={}, id товара={}, количество={}",
                saved.getId(), orderId, product.getId(), saved.getQuantity());

        return orderItemMapper.toDto(saved);
    }

    @Transactional
    public OrderItemResponseDto updateQuantity(Long orderItemId, UpdateOrderItemQuantityRequestDto dto) {
        log.info("Обновление количества позиции заказа: id позиции={}, новое количество={}",
                orderItemId, dto.getQuantity());

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemIdException(orderItemId));

        log.info("Старое количество={}, новое количество={}",
                orderItem.getQuantity(), dto.getQuantity());

        orderItem.setQuantity(dto.getQuantity());

        OrderItem updated = orderItemRepository.save(orderItem);

        log.info("Количество позиции заказа успешно обновлено, id позиции={}, количество={}",
                updated.getId(), updated.getQuantity());

        return orderItemMapper.toDto(updated);
    }

    @Transactional
    public void deleteOrderItem(Long orderItemId) {
        log.info("Удаление позиции заказа с id={}", orderItemId);

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemIdException(orderItemId));

        orderItemRepository.delete(orderItem);

        log.info("Позиция заказа успешно удалена, id позиции={}", orderItemId);
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> getItemsByOrderId(Long orderId) {
        log.info("Получение позиций заказа с id={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdException(orderId));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        log.info("Найдено {} позиций для заказа с id={}", orderItems.size(), orderId);

        return orderItemMapper.toDtoList(orderItems);
    }
}