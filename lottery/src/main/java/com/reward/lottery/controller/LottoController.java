package com.reward.lottery.controller;

import com.github.pagehelper.PageInfo;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.service.LottoService;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("lotto")
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @RequestMapping("save")
    @ResponseBody
    public String saveLottoStatistics(){
        lottoService.save();
        return "保存成功！";
    }

    @RequestMapping("saveLast")
    @ResponseBody
    public String saveLastLottoInfo(){
        lottoService.saveLast();
        return "保存成功！";
    }

    @RequestMapping("randomNumber.do")
    @ResponseBody
    public List<String> randomNumber(){
        return LotteryUtils.randomLottery("dlt");
    }

    @RequestMapping("index/{start}")
    public String index(@PathVariable(value = "start") Integer start, Model model){
        List<Lotto> lottos = lottoService.queryAll(start);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        model.addAttribute("pageInfo", pageInfo);
        return "index";
    }

    @GetMapping
    @ResponseBody
    public PageInfo<Lotto> queryAll(){
        List<Lotto> lottos = lottoService.queryAll(1);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        return pageInfo;
    }

    @GetMapping("lastLotto")
    @ResponseBody
    public Lotto queryLastLottoInfo(){
        return LotteryUtils.setAndReturnLotto(LotteryStatisticsUtils.getLastLotteryInfo("dlt"));
    }

    @GetMapping("{id}")
    @ResponseBody
    public Lotto queryById(@PathVariable("id") String id){
        return lottoService.queryById(id);
    }

    @GetMapping("queryByIssueNumber/{issueNumber}")
    @ResponseBody
    public Lotto queryByIssueNumber(@PathVariable("issueNumber") String issueNumber){
        return lottoService.queryByIssueNumber(issueNumber);
    }

    @GetMapping("queryByDate/{date}")
    @ResponseBody
    public Lotto queryByDate(@PathVariable("date") String date){
        return lottoService.queryByAwardDate(date);
    }

}
