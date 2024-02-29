/*
 * Copyright (C) 2024 Emmanuel Godwin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marktplatz.orderservice;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository repository;
  private final OrderItemRepository itemRepository;

  public OrderService(OrderRepository repository, OrderItemRepository itemRepository) {
    this.repository = repository;
    this.itemRepository = itemRepository;
  }

  @Transactional
  public Order createOrder(List<Map<Long, Integer>> cart) {
    Order order = new Order(OrderStatus.PENDING);
    repository.save(order);
    List<OrderItem> items =
        cart.stream()
            .flatMap(e -> e.entrySet().stream())
            .map(
                e -> {
                  OrderItem item = new OrderItem();
                  item.setProductId(e.getKey());
                  item.setQuantity(e.getValue());
                  item.setOrder(order);
                  return item;
                })
            .toList();
    itemRepository.saveAll(items);

    return order;
  }

  public Order getOrderById(UUID id) {
    return repository.findById(id).orElseThrow();
  }

  public OrderDetail getOrderInfo(UUID id) {
    Order order = getOrderById(id);
    List<OrderItem> items = repository.findAllOrderItems(order.getId());

    return new OrderDetail(order, items);
  }
}
