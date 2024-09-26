package jpabook.jpashop.domain;


import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable // 값 타입을 정의
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

