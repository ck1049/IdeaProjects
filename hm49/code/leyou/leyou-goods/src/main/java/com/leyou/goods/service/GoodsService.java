package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;


    public Map<String, Object> loadData(Long spuId) {
        Spu spu = this.goodsClient.querySpuBySpuId(spuId);
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        // 查询规格参数组
        List<SpecGroup> groups = this.specificationClient.querySpecsByCid(spu.getCid3());
        // 查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        // 查询分类名
        List<String> names = this.categoryClient.queryNameByIds(cids);
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }
        // 查询特殊规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        Map<Long, Object> paramMap = new HashMap<>();
        specParams.forEach(specParam -> paramMap.put(specParam.getId(), specParam.getName()));
        Map<String, Object> model = new HashMap<>();
        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        model.put("skus", skus);
        model.put("categories", categories);
        model.put("brand", brand);
        model.put("groups", groups);
        model.put("paramMap", paramMap);
        return model;
    }
}
