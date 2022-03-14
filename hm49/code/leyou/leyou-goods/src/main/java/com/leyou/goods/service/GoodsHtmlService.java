package com.leyou.goods.service;

import com.leyou.common.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {

    @Autowired
    TemplateEngine engine;

    @Autowired
    GoodsService goodService;

    public void createGoodsHtml(Long spuId){
        PrintWriter writer = null;
        try {
            Context context = new Context();
            context.setVariables(goodService.loadData(spuId));
            String PATH = "D:\\tools\\nginx-1.14.0\\html\\item\\" + spuId + ".html";
            File file = new File(PATH);
            writer = new PrintWriter(file);
            engine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId){
        ThreadUtils.execute(() -> createGoodsHtml(spuId));
    }

    public void deleteGoodsHtml(Long id) {
        String PATH = "D:\\tools\\nginx-1.14.0\\html\\item\\" + id + ".html";
        File file = new File(PATH);
        file.deleteOnExit();
    }
}
