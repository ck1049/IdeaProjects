package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String keyPrefix = "user:verify:";


    public Boolean check(String data, Integer type) {
        User user = new User();
        if (type == 1){
            user.setUsername(data);
        }else if (type == 2){
            user.setPhone(data);
        }else {
            return null;
        }
        return this.userMapper.selectCount(user) == 0;
    }

    public void sendVerifyCode(String phone) {
        if(StringUtils.isBlank(phone)){
            return;
        }
        // 生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        // 发送消息
        amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "verifycode.sms", msg);
        // 存到redis
        redisTemplate.opsForValue().set(keyPrefix + phone, code, 60, TimeUnit.SECONDS);
    }

    public Boolean register(User user, String code) {
        String redisCode = redisTemplate.opsForValue().get(keyPrefix + user.getPhone());
        if (!StringUtils.equals(code, redisCode)){
            return false;
        }
        String salt = CodecUtils.generateSalt();
        String password = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setId(null);
        user.setPassword(password);
        user.setSalt(salt);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
        redisTemplate.delete(keyPrefix + user.getPhone());
        return true;
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null){
            return null;
        }
        String salt = user.getSalt();
        if(StringUtils.equals(CodecUtils.md5Hex(password, salt), user.getPassword())){
            return user;
        }
        return null;
    }
}
