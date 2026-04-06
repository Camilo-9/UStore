
package edu.unimagdalena.uStore.enums;

public enum OrderStatus{
    CREATED,
    PAID,
    SHIPPED,            // Un pedido SHIPPED no podrá cancelarse.
    DELIVERED,
    CANCELLED
}
