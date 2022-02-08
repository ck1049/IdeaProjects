package cn.itcast.elasticsearch.test;

import cn.itcast.elasticsearch.pojo.Item;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private ElasticsearchOperations operations;

    @Test
    public void testIndex() throws IOException {
        IndexOperations indexOps = this.restTemplate.indexOps(Item.class);
    }

}
