package com.reward.lottery.service;

import com.github.pagehelper.PageHelper;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.mapper.LottoDao;
import com.reward.lottery.utils.DateUtils;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class LottoService {

    @Autowired
    private LottoDao lottoDao;

    public void save(){
        List<Map<String, String>> list = LotteryStatisticsUtils.historyList("dlt");
        for (Map<String, String> map: list) {
            lottoDao.insert(LotteryUtils.setAndReturnLotto(map));
        }
    }

    public void saveLast() {
        Lotto lastlotto = getLastlotto();
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", lastlotto.getIssueNumber());
        Lotto lotto = lottoDao.selectOneByExample(example);
        if (lotto == null){
            lottoDao.insert(lastlotto);//根据期号查询本地数据库，没有该条记录时进行插入
        }
    }

    public List<Lotto> queryAll(Integer start){
        Example example = new Example(Lotto.class);
        example.orderBy("issueNumber").desc();
        PageHelper.startPage(start, 20);
        return lottoDao.selectByExample(example);
    }

    public Lotto queryById(String id){
        return lottoDao.selectByPrimaryKey(id);
    }

    /**
     * 获取最近一期大乐透
     * @return
     */
    public Lotto getLastlotto(){
        Map<String, String> lottoMap = LotteryStatisticsUtils.getLastLotteryInfo("dlt");
        return LotteryUtils.setAndReturnLotto(lottoMap);
    }

    public void create(Lotto lotto){
        lottoDao.insert(lotto);
    }

    public Lotto queryByIssueNumber(String issueNumber) {
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", issueNumber);
        return lottoDao.selectOneByExample(example);
    }


    public Lotto queryByAwardDate(String date) {
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("awardDate", DateUtils.formatDateToString(date));
        return lottoDao.selectOneByExample(example);
    }
}
