package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity     // 데이터베이스의 테이블과 일대일로 매칭되는 객체 단위
@Getter @Setter
public class Member {
    @Id @GeneratedValue     // id 어노테이션은 primary key를 의미 , GenerateValue ID 값을 entity가 생성될때마다 자동 생성되게 해주는 annotation
    @Column(name = "member_id")     // table과 매핑할 때  column의 이름을 지정한다.
    private Long id;

    @NotEmpty
    private String name;


    @Embedded       // 값 타입을 사용하는 곳에 표시
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") // 해당 member는 order테이블에 의해서 매핑
    private List<Order> orders = new ArrayList<>();


}
