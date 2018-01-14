package com.btb.zb;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.JsoupUtil;
import com.btb.util.StringUtil;
import com.btb.util.dao.BaseDaoSql;
import com.btb.zb.vo.MarketHistoryVo1;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhenya.1291813139.com
 * on 2018/1/15.
 * btb_grab.
 */
@Service("ZBhttp")
public class HttpUtil extends BaseHttp {
    public String getPlatformId() {
        //必须跟数据库的平台id一致

        return "ZB";
    }

    /**
     * 获取第三方交易对
     * @return
     */
    public void geThirdpartysupportmoneys(List<PlatformSupportmoney> thirdpartysupportmoneys) {
        String url = "http://api.zb.com/data/v1/markets";
        String result = JsoupUtil.getJson(url);
        if (result != null) {
            Map map = JSON.parseObject(result, Map.class);
            for (Object map2 : map.keySet()) {
                String moneypair = map2.toString();
                PlatformSupportmoney thirdpartysupportmoney = new PlatformSupportmoney();
                thirdpartysupportmoney.setPlatformid(getPlatformId());
                String[] split = moneypair.split("_");
                thirdpartysupportmoney.setMoneytype(split[0]);
                thirdpartysupportmoney.setBuymoneytype(split[1]);
                thirdpartysupportmoney.setMoneypair(moneypair);
                thirdpartysupportmoneys.add(thirdpartysupportmoney);
            }
        }
    }

    /**
     * marketHistory:有两个数据, 平台id,和交易对
     * marketHistoryMapper: 保存数据的dao层对象
     * size: 要查询的size, 通过当前数据最大时间,和当前时间差计算而来
     * dbCurrentTime: 数据库当前最大时间,long类型
     */
    public void getKLineData(Markethistory marketHistory, Long size, Long dbCurrentTime) {
        try {
            String url="http://api.zb.com/data/v1/kline?type=1min&size="+size+"&market="+marketHistory.getMoneypair();
            System.out.println(url);
            String text = JsoupUtil.getJson(url);
            if (text != null) {
                MarketHistoryVo1 marketHistoryVo1 = JSON.parseObject(text, MarketHistoryVo1.class);
                LinkedList<Object[]> data = marketHistoryVo1.getData();
                ////去除最新值,因为最新值,当前分钟还没有统计完整,

                if (!data.isEmpty()) {
                    data.removeLast();
                }

                for (Object[] marketHistoryVo2:data) {
                    Long timeid = Long.valueOf(StringUtil.valueOf(marketHistoryVo2[0]))/1000;
                    if (timeid<=dbCurrentTime) {
                        continue;//如果小于数据库最大时间,说明数据库已经存在,不需要再添加

                    }else {
                        marketHistory.setTimeid(timeid);//这里需要注意,long类型时间必须为10位的,msql数据库才支持

                        marketHistory.setAmount(StringUtil.toBigDecimal(marketHistoryVo2[5]));
                        marketHistory.setClose(StringUtil.toBigDecimal(marketHistoryVo2[4]));
                        //marketHistory.setCount(marketHistoryVo2.getCount());

                        marketHistory.setHigh(StringUtil.toBigDecimal(marketHistoryVo2[2]));
                        marketHistory.setLow(StringUtil.toBigDecimal(marketHistoryVo2[1]));
                        marketHistory.setOpen(StringUtil.toBigDecimal(marketHistoryVo2[2]));
                        //marketHistory.setVol(marketHistoryVo2.getVol());

                        BaseDaoSql.save(marketHistory);//保存到数据库

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        BaseHttp.testLoadKline(HttpUtil.class);
        //BaseHttp.testLoadMoneyPair(HttpUtil.class);

    }
}
