package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity //JPA에서 관린하는것으로 알려야함.
@Table(name="item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Column(name="item_name", length = 10)*/
    /*camel로 인하여 작성안해도 된다.*/
    private String itemName;
    private Integer price;
    private Integer quantity;

    /*생성자 필수*/
    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
