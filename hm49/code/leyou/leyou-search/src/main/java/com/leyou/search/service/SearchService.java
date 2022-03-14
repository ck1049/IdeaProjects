package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private ElasticsearchRestTemplate restTemplate;
    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public SearchResult search(SearchRequest request) {
        if (StringUtils.isBlank(request.getKey())){
            return null;
        }
        // 自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加查询条件
        //QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
        BoolQueryBuilder boolQueryBuilder = buildBoolQueryBuilder(request);
        queryBuilder.withQuery(boolQueryBuilder);
        // 添加分页条件
        queryBuilder.withPageable(PageRequest.of(request.getPage() - 1, request.getSize()));
        // 添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "all", "subTitle", "skus"}, null));
        // 添加分类、品牌聚合
        String categoryAggName = "categories";
        String brandAggName = "brands";
        queryBuilder.withAggregations(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.withAggregations(AggregationBuilders.terms(brandAggName).field("brandId"));
        HighlightBuilder.Field all = new HighlightBuilder.Field("all");
        all.preTags("<span style='color:red'>");
        all.postTags("</span>");
        queryBuilder.withHighlightFields(all);
        SearchHits<Goods> goodsSearchHits = this.restTemplate.search(queryBuilder.build(), Goods.class);
        long total = goodsSearchHits.getTotalHits();
        long totalPages = total / request.getSize();
        List<SearchHit<Goods>> goodsSearchHitList = goodsSearchHits.getSearchHits();
        List<Goods> goodList = goodsSearchHitList.stream().map(hit -> {
            Goods goods = hit.getContent();
            goods.setAll(hit.getHighlightField("all").get(0));
            return goods;
        }).collect(Collectors.toList());
        ElasticsearchAggregations elasticsearchAggregations = (ElasticsearchAggregations)goodsSearchHits.getAggregations();
        assert elasticsearchAggregations != null;
        Aggregations aggregations = elasticsearchAggregations.aggregations();
        // 解析分类聚合
        List<Map<String, Object>> categories = getCategoryAggregation(aggregations, categoryAggName);
        // 解析品牌聚合
        List<Brand> brands = getBrandAggregation(aggregations, brandAggName);
        // 获取并解析规格参数聚合
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1){
            // 只有一个分类的时候才添加规格参数聚合
            specs = getSpecAggregation((Long) categories.get(0).get("id"), boolQueryBuilder);
        }
        return new SearchResult(total, (int)totalPages, goodList, categories, brands, specs);
    }

    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        Map<String, Object> filter = request.getFilter();
        filter.entrySet().forEach(entry -> {
            String key = entry.getKey();
            if (StringUtils.equals("品牌", key)){
                key = "brandId";
            }else if (StringUtils.equals("分类", key)){
                key = "cid3";
            }else {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        });
        return boolQueryBuilder;
    }

    private List<Map<String, Object>> getSpecAggregation(Long cid, QueryBuilder queryBuilder) {
        // 创建自定义查询构建器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 添加基本查询
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        // 添加结果集过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        // 根据分类id查询需要聚合的规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, cid, null, true);
        specParams.forEach(specParam -> {
            nativeSearchQueryBuilder.withAggregations(AggregationBuilders.terms(specParam.getName()).field("specs." + specParam.getName() + ".keyword"));
        });
        SearchHits<Goods> search = this.restTemplate.search(nativeSearchQueryBuilder.build(), Goods.class);
        ElasticsearchAggregations elasticsearchAggregations = (ElasticsearchAggregations)search.getAggregations();
        // 获取聚合map集合
        Map<String, Aggregation> aggregationMap = elasticsearchAggregations.aggregations().asMap();
        return aggregationMap.entrySet().stream().map(entry -> {
            Map<String, Object> specMap = new HashMap<>();
            String key = entry.getKey();
            Terms terms = (Terms)entry.getValue();
            List<String> options = terms.getBuckets().stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList());
            specMap.put("k", key);
            specMap.put("options", options);
            return specMap;
        }).collect(Collectors.toList());
    }

    private List<Brand> getBrandAggregation(Aggregations aggregations, String brandAggName) {
        Terms terms = aggregations.get(brandAggName);
        return terms.getBuckets().stream().map(bucket -> {
            long brandId = bucket.getKeyAsNumber().longValue();
            return this.brandClient.queryBrandById(brandId);
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getCategoryAggregation(Aggregations aggregations, String categoryAggName) {
        Terms terms = aggregations.get(categoryAggName);
        return terms.getBuckets().stream().map(bucket -> {
            Map<String, Object> map = new HashMap<>();
            long cid = bucket.getKeyAsNumber().longValue();
            List<String> categoryList = this.categoryClient.queryNameByIds(Arrays.asList(cid));
            map.put("id", cid);
            map.put("name", categoryList.get(0));
            return map;
        }).collect(Collectors.toList());
    }

    public Goods buildGoods(Spu spu) throws JsonProcessingException {
        Goods goods = new Goods();
        // 根据分类的id查询分类名称
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 根据品牌id查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        // 收集sku的必要字段信息
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        skus.forEach(sku -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            // 获取sku中的图片，数据库中的图片可能是多张，多张是以","分隔，所以也以","来切割返回图片数组，获取第一张图片
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuMapList.add(map);
        });
        // 根据spu中的cid3查询出所有的搜索规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, spu.getCid3(), null, true);
        // 根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        // 把通用的规格参数值，进行反序列化
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>(){});
        // 把特殊的规格参数值，进行反序列化
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>(){});
        Map<String, Object> specs = new HashMap<>();
        specParams.forEach(param -> {
            // 判断规格参数的类型是否是通用的规格参数
           if (param.getGeneric()){
               // 如果是通用类型的参数，从genericSpecMap获取规格参数值
               String value = genericSpecMap.get(param.getId().toString()).toString();
               // 判断是否是数值类型，如果是数值类型，应该返回一个区间
               if (param.getNumeric()){
                   value = chooseSegment(value, param);
               }
               specs.put(param.getName(), value);
           }else {
               List<Object> value = specialSpecMap.get(param.getId().toString());
               specs.put(param.getName(), value);
           }
        });
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        // 拼接all字段，需要分类名称及品牌名称
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " ") + " " + brand.getName());
        // 获取spu下所有sku的价格
        goods.setPrice(skus.stream().map(Sku::getPrice).collect(Collectors.toList()));
        // 获取spu下的所有的sku，并转化成json字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        // 获取所有查询的规格参数{name:value}
        goods.setSpecs(specs);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public void save(Long id) throws JsonProcessingException {
        Spu spu = this.goodsClient.querySpuBySpuId(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    public void delete(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
