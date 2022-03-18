package com.leyou.cart.service;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:cart";

    /**
     * 添加购物车
     * @param cart
     */
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();
        // 判断当前的商品是否在购物车中
        if(Boolean.TRUE.equals(hashOperations.hasKey(key))){
            // 在，更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        }else {
            // 不在，新增购物车
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
        }
        hashOperations.put(key, JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车
     * @return
     */
    public List<Cart> queryCarts() {
        // 获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 判断该用户是否有购物车
        if (Boolean.FALSE.equals(this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId()))){
            return null;
        }
        // 获取购物车集合
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        // 判断购物车集合是否为空
        List<Object> cartsJson = hashOperations.values();
        if (CollectionUtils.isEmpty(cartsJson)){
            return null;
        }
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());
    }

    /**
     * 修改购物车商品信息
     * @param cart
     */
    public void updateCart(Cart cart) {
        // 获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        if (userInfo == null){
            return;
        }

        // 判断是否有该用户的购物车信息
        if(Boolean.FALSE.equals(this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId()))){
            return;
        }

        // 判断用户购物车是否为空
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        if (CollectionUtils.isEmpty(hashOperations.values())){
            return;
        }

        // 更新购物车商品信息
        Integer num = cart.getNum();
        cart = JsonUtils.parse(hashOperations.get(cart.getSkuId().toString()).toString(), Cart.class);
        cart.setNum(num);
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        this.redisTemplate.boundHashOps(KEY_PREFIX + LoginInterceptor.getUserInfo().getId()).delete(skuId);
    }

    public void addCarts(List<Cart> carts) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        for (Cart cart : carts) {
            String key = cart.getSkuId().toString();
            Integer num = cart.getNum();
            // 判断当前的商品是否在购物车中
            if(Boolean.TRUE.equals(hashOperations.hasKey(key))){
                // 在，更新数量
                String cartJson = hashOperations.get(key).toString();
                cart = JsonUtils.parse(cartJson, Cart.class);
                cart.setNum(cart.getNum() + num);
            }else {
                // 不在，新增购物车
                Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
                cart.setUserId(userInfo.getId());
                cart.setTitle(sku.getTitle());
                cart.setOwnSpec(sku.getOwnSpec());
                cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
                cart.setPrice(sku.getPrice());
            }
            hashOperations.put(key, JsonUtils.serialize(cart));
        }

    }
}
