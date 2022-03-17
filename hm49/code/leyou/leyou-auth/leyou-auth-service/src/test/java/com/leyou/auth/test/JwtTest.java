package com.leyou.auth.test;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RsaUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "D:\\IdeaProjects\\hm49\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\IdeaProjects\\hm49\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @BeforeEach
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6NTksInVzZXJuYW1lIjoiY2FveXVua2UiLCJleHAiOjE2NDc0MDg3MDF9.YtCw2rU1NsFZtPAO1aK749t-_WGHT4hpyM9vKmnKIhKJ-cVJATuiopQQ36ooX8_-R3igCMwuf2rjJ9alVaxb7KJRb8G4BbNJlT06FH8DKnzDQysu8ne9zZa9IrojvP3QSbqTPaixIBiMBiNRgEkLdkV6dU0sK9ozNWkbtvhaOKU";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}