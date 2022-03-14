package com.leyou.sms.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.leyou.sms.config.SmsConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(SmsConfigure.class)
public class SmsUtils {

    @Autowired
    private SmsConfigure config;

    static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    public SendSmsResponse sendSms(String phone, String code) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = this.createClient(this.config.getAccessKeyId(), this.config.getAccessKeySecret());
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSignName("阿里云短信测试");
        sendSmsRequest.setTemplateCode("SMS_154950909");
        sendSmsRequest.setPhoneNumbers(phone);
        sendSmsRequest.setTemplateParam("{\"code\":" + code + "}");
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        logger.info("发送短信状态：{}", sendSmsResponse.getBody().getCode());
        logger.info("发送短信消息：{}", sendSmsResponse.getBody().getMessage());
        return sendSmsResponse;
    }
}
