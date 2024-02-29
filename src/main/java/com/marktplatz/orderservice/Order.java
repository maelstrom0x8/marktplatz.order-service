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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @Column(name = "order_id")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
  @GenericGenerator(
      name = "pg-uuid",
      strategy = "uuid2",
      parameters =
          @org.hibernate.annotations.Parameter(
              name = "uuid_gen_strategy_class",
              value = "com.marktplatz.orderservice.PostgresUuidGeneratorStrategy"))
  private UUID id;

  @Column(name = "status", columnDefinition = "order_status")
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @CreationTimestamp private LocalDateTime createdAt;

  private LocalDateTime lastModified;

  @PrePersist
  public void onInsert() {
    createdAt = LocalDateTime.now();
    lastModified = createdAt;
  }

  @PreUpdate
  public void onUpdate() {
    lastModified = LocalDateTime.now();
  }

  public Order() {}

  public Order(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public UUID getId() {
    return id;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }
}
