package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter

public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;

    private int count;

    // == 생성 메서드 == //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removestock(count);
        return orderItem;
    }

    // == 비지니스 로직 ==//
    public void cancel() {
        getItem().addstock(count);      // 재고수량 원복
    }

    // == 조회 로직 == //

    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }

}
