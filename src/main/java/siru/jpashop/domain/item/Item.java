package siru.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import siru.jpashop.domain.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;;

    private int price;

    private int stockQuantity;

    @ManyToMany
    private List<Category> categories  = new ArrayList<>();

}
