package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    /**
     * 根据条件分页查询spu
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    /**
     * 根据spuId查询spuDetail
     */
    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据spuId查询sku集合
     */
    @GetMapping("sku/list")
    List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据spuId查询spu
     * @param spuId
     * @return
     */
    @GetMapping("spu/{spuId}")
    Spu querySpuBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    Sku querySkuBySkuId(@PathVariable("skuId") Long skuId);
}
