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

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_gen")
  @SequenceGenerator(
      sequenceName = "order_item_seq",
      name = "order_item_gen",
      initialValue = 200,
      allocationSize = 1)
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  public OrderItem() {}

  public OrderItem(Long id, Order order, Integer quantity, Long productId) {
    this.id = id;
    this.order = order;
    this.quantity = quantity;
    this.productId = productId;
  }

  public Long getId() {
    return id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }
}
