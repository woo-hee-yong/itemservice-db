package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
/*@Transactional 전체에 트랜젝션을 걸고 싶을때. */
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }


    @Override
    @Transactional /*Jpa에서 데이터 변경시에는 항상 있어야함*/
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    @Transactional /*Jpa에서 데이터 변경시에는 항상 있어야함*/
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        //여기까지 셋팅하면 update는 자동으로 진행한다.
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from Item i"; // Item 객체를 말한다. i는 Entity 자체
        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();
        List<Item> result = em.createQuery(jpql, Item.class).getResultList();

        if(StringUtils.hasText(itemName) || maxPrice != null){
            jpql +=" where";
        }

        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if(StringUtils.hasText(itemName)){
            jpql += " i.itemName like concat('%', :itemName, '%')";
            param.add(itemName);
            andFlag= true;
        }

        if(maxPrice != null){
            if(andFlag){
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
            param.add(maxPrice);
        }

        log.info("jpql={}", jpql);

        TypedQuery<Item> query  = em.createQuery(jpql, Item.class);
        if(StringUtils.hasText(itemName)){
            query.setParameter("itemName", itemName);
        }

        if(maxPrice != null){
            query.setParameter("maxPrice", maxPrice);
        }

        return query.getResultList();
    }
}
