package cn.itcast.elasticsearch.dao;

import cn.itcast.elasticsearch.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item, Long> {
    List<Item> findByTitle(String title);

    List<Item> findByPriceBetween(Double price1, Double price2);
}
