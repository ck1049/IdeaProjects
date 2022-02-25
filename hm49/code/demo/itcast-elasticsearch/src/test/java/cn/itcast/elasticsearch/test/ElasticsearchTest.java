package cn.itcast.elasticsearch.test;

import cn.itcast.elasticsearch.dao.ItemRepository;
import cn.itcast.elasticsearch.pojo.Item;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SearchExecutionContext;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Test
    public void testIndex() throws IOException {
        IndexOperations indexOps = this.restTemplate.indexOps(Item.class);
        indexOps.create();
    }

    @Test
    public void testDelete() throws IOException {
        this.restTemplate.indexOps(Item.class).delete();
    }

    @Test
    public void testSave() throws IOException {
        Item item = new Item();
        item.setId(4L);
        item.setBrand("huawei");
        item.setCategory("phone");
        item.setTitle("华为手机 2T 200T");
        item.setImages("http://image.leyou.com/group1/M00/00/00/wKgjgmH_yx2AXT6KAABBIjJcuLQ069.png");
        item.setPrice(29999.0d);
        this.restTemplate.save(item);
    }

    @Test
    public void testQuery(){
        SearchHits<Item> search = this.restTemplate.search((Query) Query.findAll().addSort(Sort.by("price").descending()), Item.class);
        search.forEach(System.out::println);
    }

    @Test
    public void testFindByTitle(){
        List<Item> items = itemRepository.findByTitle("手机");
        items.forEach(System.out::println);
    }

    @Test
    public void testFindByPrice(){
        List<Item> items = itemRepository.findByPriceBetween(10000.0d, 13000.0d);
        items.forEach(System.out::println);
    }

    @Test
    public void testSearch(){
        // 匹配查询构建器设置查询字段，查询关键字
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "手机");
        // 添加分页查询条件（页码从0开始）
        PageRequest pageRequest = PageRequest.of(0, 2);
        // 创建自定义查询构建器，build查询对象，设置查询条件，添加分页设置，设置按价格倒序排序
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQueryBuilder).withPageable(pageRequest).withSorts(SortBuilders.fieldSort("price").order(SortOrder.DESC)).build();
        // 根据设置的自定义查询类进行搜索
        SearchHits<Item> items = this.restTemplate.search(searchQuery, Item.class);
        items.forEach(System.out::println);
        System.out.println(items.getTotalHits());
        System.out.println(items.getSearchHits());
    }

    @Test
    public void testAggs() {
        // 初始化自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        String aggName = "brandAgg";
        // 添加聚合
        queryBuilder.withAggregations(AggregationBuilders.terms(aggName).field("brand"));
        // 添加结果集过滤 不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        // 执行查询
        SearchHits<Item> itemSearchHits = this.restTemplate.search(queryBuilder.build(), Item.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations)itemSearchHits.getAggregations();
        assert aggregations != null;
        Aggregations aggregations1 = aggregations.aggregations();
        // 解析聚合
        Terms terms = (Terms)aggregations1.get(aggName);
        List<Aggregation> aggregationList = aggregations1.asList();
        terms.getBuckets().forEach(bucket -> {
            System.out.println(bucket.getKey() + ":" + bucket.getDocCount());
        });
    }

    @Test
    public void testSubAggs() {
        // 初始化自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        String aggName = "brandAgg";
        // 添加聚合、度量
        queryBuilder.withAggregations(AggregationBuilders.terms(aggName).field("brand")
                .subAggregation(AggregationBuilders.avg("price_avg").field("price")));
        // 添加结果集过滤 不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        // 执行查询
        SearchHits<Item> itemSearchHits = this.restTemplate.search(queryBuilder.build(), Item.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations)itemSearchHits.getAggregations();
        assert aggregations != null;
        Aggregations aggregations1 = aggregations.aggregations();
        // 解析聚合
        Terms terms = (Terms)aggregations1.get(aggName);
        List<Aggregation> aggregationList = aggregations1.asList();
        terms.getBuckets().forEach(bucket -> {
            System.out.println(bucket.getKey() + ":" + bucket.getDocCount());
            Map<String, Aggregation> map = bucket.getAggregations().asMap();
            Avg price_avg = (Avg)map.get("price_avg");
            System.out.println(price_avg.getValue());
        });
    }
}
