package io.factorialsystems.msscstoreorders.entity;

public enum OrderStatus {
    ORDER_NEW(1), ORDER_PAID (2), ORDER_SHIPPED (3);

    private final int value;
//    private static final Map<Integer, OrderStatus> map = new HashMap<>();
    OrderStatus(int value) {
        this.value = value;
    }

//    static {
//        for (OrderStatus orderStatus: OrderStatus.values()) {
//            map.put(orderStatus.value, orderStatus);
//        }
//    }

//    public static OrderStatus valueOf(int orderType) {
//        return (OrderStatus) map.get(orderType);
//    }

    public int getValue() {
        return value;
    }
}
