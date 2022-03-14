package com.leyou.elasticsearch.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    private ElasticsearchRestTemplate template;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test(){
        Document mapping = this.template.indexOps(Goods.class).createMapping();
        this.template.indexOps(Goods.class).putMapping(mapping);

        Integer page = 1;
        Integer rows = 100;
        do {
            // 分页查询
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, null, page, rows);
            List<SpuBo> spuBoList = result.getItems();
            List<Goods> goods = spuBoList.stream().map(spuBo -> {
                try {
                    return searchService.buildGoods(spuBo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            goodsRepository.saveAll(goods);
            rows = spuBoList.size();
            page++;
        }while (rows == 100);
    }

}
