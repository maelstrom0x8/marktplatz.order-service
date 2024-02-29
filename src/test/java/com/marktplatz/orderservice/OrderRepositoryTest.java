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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest(properties = "{spring.datasource.url=jdbc:tc:postgresql:15:///test}")
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
class OrderRepositoryTest {

  @Autowired private OrderRepository orderRepository;
  @Autowired private OrderItemRepository itemRepository;

  @Test
  void allOrderItemsHaveCorrectOrderId() {
    Order order = new Order(OrderStatus.PENDING);
    orderRepository.save(order);
    List<OrderItem> items = mapToItems(Map.of(234L, 5, 5644L, 1, 5653L, 6), order);
    itemRepository.saveAll(items);

    List<OrderItemDTO> allOrderItems = orderRepository.findAllOrderItems(order.getId());
    assertThat(allOrderItems).hasSize(3);
    assertThat(allOrderItems)
        .extracting(OrderItemDTO::orderId)
        .allMatch(e -> e.equals(order.getId()));
  }

  private List<OrderItem> mapToItems(Map<Long, Integer> cart, Order order) {
    return cart.entrySet().stream()
        .map(
            f -> {
              OrderItem item = new OrderItem();
              item.setProductId(f.getKey());
              item.setQuantity(f.getValue());
              item.setOrder(order);
              return item;
            })
        .toList();
  }
}
