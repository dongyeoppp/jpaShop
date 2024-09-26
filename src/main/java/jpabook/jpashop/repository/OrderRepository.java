package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    public final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;
        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            }else{
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null){
            query = query.setParameter("status",orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())){
            query = query.setParameter("name",orderSearch.getMemberName());
        }
        return query.getResultList();
    }


    public List<Order> findAllWithMemberDelivery() {        // 재사용성이 좋다. 로직 재활용 가능
        return em.createQuery(
                "select o from Order o" +           // join fetch : lazy 무시 하고 한번에 싹다 가져오기
                    " join fetch o.member m" +
                    " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {  // 화면에 최적화 되어있지만 재사용성이 낮음
        return em.createQuery(
                        "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();

    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();


    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                    "select o from Order o" +           // join fetch : lazy 무시 하고 한번에 싹다 가져오기
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
