package com.alipay.demo.trade;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.*;
import com.alipay.demo.trade.model.hb.*;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyangkly on 15/8/9.
 * 绠€鍗昺ain鍑芥暟锛岀敤浜庢祴璇曞綋闈粯api
 * sdk鍜宒emo鐨勬剰瑙佸拰闂鍙嶉璇疯仈绯伙細liuyang.kly@alipay.com
 */
public class Main {
    private static Log log = LogFactory.getLog(Main.class);

    // 鏀粯瀹濆綋闈粯2.0鏈嶅姟
    private static AlipayTradeService tradeService;

    // 鏀粯瀹濆綋闈粯2.0鏈嶅姟锛堥泦鎴愪簡浜ゆ槗淇濋殰鎺ュ彛閫昏緫锛�
    private static AlipayTradeService tradeWithHBService;

    // 鏀粯瀹濅氦鏄撲繚闅滄帴鍙ｆ湇鍔★紝渚涙祴璇曟帴鍙pi浣跨敤锛岃鍏堥槄璇籸eadme.txt
    private static AlipayMonitorService monitorService;

    static {
        /**
         * 涓€瀹氳鍦ㄥ垱寤篈lipayTradeService涔嬪墠璋冪敤Configs.init()璁剧疆榛樿鍙傛暟
         * Configs浼氳鍙朿lasspath涓嬬殑zfbinfo.properties鏂囦欢閰嶇疆淇℃伅锛屽鏋滄壘涓嶅埌璇ユ枃浠跺垯纭璇ユ枃浠舵槸鍚﹀湪classpath鐩綍
         */
        Configs.init("zfbinfo.properties");

        /**
         * 浣跨敤Configs鎻愪緵鐨勯粯璁ゅ弬鏁�
         * AlipayTradeService鍙互浣跨敤鍗曚緥鎴栬€呬负闈欐€佹垚鍛樺璞★紝涓嶉渶瑕佸弽澶峮ew
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 鏀粯瀹濆綋闈粯2.0鏈嶅姟锛堥泦鎴愪簡浜ゆ槗淇濋殰鎺ュ彛閫昏緫锛�
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /**
         * 濡傛灉闇€瑕佸湪绋嬪簭涓鐩朇onfigs鎻愪緵鐨勯粯璁ゅ弬鏁�, 鍙互浣跨敤ClientBuilder绫荤殑setXXX鏂规硶淇敼榛樿鍙傛暟
         * 鍚﹀垯浣跨敤浠ｇ爜涓殑榛樿璁剧疆
         */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
    }

    // 绠€鍗曟墦鍗板簲绛�
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    public static void main(String[] args) {
        Main main = new Main();

        // 绯荤粺鍟嗗晢娴嬭瘯浜ゆ槗淇濋殰鎺ュ彛api
        // main.test_monitor_sys();

        // POS鍘傚晢娴嬭瘯浜ゆ槗淇濋殰鎺ュ彛api
        // main.test_monitor_pos();

        // 娴嬭瘯浜ゆ槗淇濋殰鎺ュ彛璋冨害
        // main.test_monitor_schedule_logic();

        // 娴嬭瘯褰撻潰浠�2.0鏀粯锛堜娇鐢ㄦ湭闆嗘垚浜ゆ槗淇濋殰鎺ュ彛鐨勫綋闈粯2.0鏈嶅姟锛�
        // main.test_trade_pay(tradeService);

        // 娴嬭瘯鏌ヨ褰撻潰浠�2.0浜ゆ槗
        // main.test_trade_query();

        // 娴嬭瘯褰撻潰浠�2.0閫€璐�
        // main.test_trade_refund();

        // 娴嬭瘯褰撻潰浠�2.0鐢熸垚鏀粯浜岀淮鐮�
        main.test_trade_precreate();
    }

    // 娴嬭瘯绯荤粺鍟嗕氦鏄撲繚闅滆皟搴�
    public void test_monitor_schedule_logic() {
        // 鍚姩浜ゆ槗淇濋殰绾跨▼
        DemoHbRunner demoRunner = new DemoHbRunner(monitorService);
        demoRunner.setDelay(5); // 璁剧疆鍚姩鍚庡欢杩�5绉掑紑濮嬭皟搴︼紝涓嶈缃垯榛樿3绉�
        demoRunner.setDuration(10); // 璁剧疆闂撮殧10绉掕繘琛岃皟搴︼紝涓嶈缃垯榛樿15 * 60绉�
        demoRunner.schedule();

        // 鍚姩褰撻潰浠橈紝姝ゅ姣忛殧5绉掕皟鐢ㄤ竴娆℃敮浠樻帴鍙ｏ紝骞朵笖褰撻殢鏈烘暟涓�0鏃朵氦鏄撲繚闅滅嚎绋嬮€€鍑�
        while (Math.random() != 0) {
            test_trade_pay(tradeWithHBService);
            Utils.sleep(5 * 1000);
        }

        // 婊¤冻閫€鍑烘潯浠跺悗鍙互璋冪敤shutdown浼橀泤瀹夊叏閫€鍑�
        demoRunner.shutdown();
    }

    // 绯荤粺鍟嗙殑璋冪敤鏍蜂緥锛屽～鍐欎簡鎵€鏈夌郴缁熷晢鍟嗛渶瑕佸～鍐欑殑瀛楁
    public void test_monitor_sys() {
        // 绯荤粺鍟嗕娇鐢ㄧ殑浜ゆ槗淇℃伅鏍煎紡锛宩son瀛楃涓茬被鍨�
        List<SysTradeInfo> sysTradeInfoList = new ArrayList<SysTradeInfo>();
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000001", 5.2, HbStatus.S));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000002", 4.4, HbStatus.F));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000003", 11.3, HbStatus.P));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000004", 3.2, HbStatus.X));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000005", 4.1, HbStatus.X));

        // 濉啓寮傚父淇℃伅锛屽鏋滄湁鐨勮瘽
        List<ExceptionInfo> exceptionInfoList = new ArrayList<ExceptionInfo>();
        exceptionInfoList.add(ExceptionInfo.HE_SCANER);
        // exceptionInfoList.add(ExceptionInfo.HE_PRINTER);
        // exceptionInfoList.add(ExceptionInfo.HE_OTHER);

        // 濉啓鎵╁睍鍙傛暟锛屽鏋滄湁鐨勮瘽
        Map<String, Object> extendInfo = new HashMap<String, Object>();
        // extendInfo.put("SHOP_ID", "BJ_ZZ_001");
        // extendInfo.put("TERMINAL_ID", "1234");

        String appAuthToken = "搴旂敤鎺堟潈浠ょ墝";// 鏍规嵁鐪熷疄鍊煎～鍐�

        AlipayHeartbeatSynRequestBuilder builder = new AlipayHeartbeatSynRequestBuilder()
                .setAppAuthToken(appAuthToken).setProduct(Product.FP).setType(Type.CR)
                .setEquipmentId("cr1000001").setEquipmentStatus(EquipStatus.NORMAL)
                .setTime(Utils.toDate(new Date())).setStoreId("store10001").setMac("0a:00:27:00:00:00")
                .setNetworkType("LAN").setProviderId("2088911212323549") // 璁剧疆绯荤粺鍟唒id
                .setSysTradeInfoList(sysTradeInfoList) // 绯荤粺鍟嗗悓姝rade_info淇℃伅
                // .setExceptionInfoList(exceptionInfoList) // 濉啓寮傚父淇℃伅锛屽鏋滄湁鐨勮瘽
                .setExtendInfo(extendInfo) // 濉啓鎵╁睍淇℃伅锛屽鏋滄湁鐨勮瘽
        ;

        MonitorHeartbeatSynResponse response = monitorService.heartbeatSyn(builder);
        dumpResponse(response);
    }

    // POS鍘傚晢鐨勮皟鐢ㄦ牱渚嬶紝濉啓浜嗘墍鏈塸os鍘傚晢闇€瑕佸～鍐欑殑瀛楁
    public void test_monitor_pos() {
        // POS鍘傚晢浣跨敤鐨勪氦鏄撲俊鎭牸寮忥紝瀛楃涓茬被鍨�
        List<PosTradeInfo> posTradeInfoList = new ArrayList<PosTradeInfo>();
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.S, "1324", 7));
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.X, "1326", 15));
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.S, "1401", 8));
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.F, "1405", 3));

        // 濉啓寮傚父淇℃伅锛屽鏋滄湁鐨勮瘽
        List<ExceptionInfo> exceptionInfoList = new ArrayList<ExceptionInfo>();
        exceptionInfoList.add(ExceptionInfo.HE_PRINTER);

        // 濉啓鎵╁睍鍙傛暟锛屽鏋滄湁鐨勮瘽
        Map<String, Object> extendInfo = new HashMap<String, Object>();
        // extendInfo.put("SHOP_ID", "BJ_ZZ_001");
        // extendInfo.put("TERMINAL_ID", "1234");

        AlipayHeartbeatSynRequestBuilder builder = new AlipayHeartbeatSynRequestBuilder()
                .setProduct(Product.FP)
                .setType(Type.SOFT_POS)
                .setEquipmentId("soft100001")
                .setEquipmentStatus(EquipStatus.NORMAL)
                .setTime("2015-09-28 11:14:49")
                .setManufacturerPid("2088000000000009")
                // 濉啓鏈哄叿鍟嗙殑鏀粯瀹漰id
                .setStoreId("store200001").setEquipmentPosition("31.2433190000,121.5090750000")
                .setBbsPosition("2869719733-065|2896507033-091").setNetworkStatus("gggbbbgggnnn")
                .setNetworkType("3G").setBattery("98").setWifiMac("0a:00:27:00:00:00")
                .setWifiName("test_wifi_name").setIp("192.168.1.188")
                .setPosTradeInfoList(posTradeInfoList) // POS鍘傚晢鍚屾trade_info淇℃伅
                // .setExceptionInfoList(exceptionInfoList) // 濉啓寮傚父淇℃伅锛屽鏋滄湁鐨勮瘽
                .setExtendInfo(extendInfo) // 濉啓鎵╁睍淇℃伅锛屽鏋滄湁鐨勮瘽
        ;

        MonitorHeartbeatSynResponse response = monitorService.heartbeatSyn(builder);
        dumpResponse(response);
    }

    // 娴嬭瘯褰撻潰浠�2.0鏀粯
    public void test_trade_pay(AlipayTradeService service) {
        // (蹇呭～) 鍟嗘埛缃戠珯璁㈠崟绯荤粺涓敮涓€璁㈠崟鍙凤紝64涓瓧绗︿互鍐咃紝鍙兘鍖呭惈瀛楁瘝銆佹暟瀛椼€佷笅鍒掔嚎锛�
        // 闇€淇濊瘉鍟嗘埛绯荤粺绔笉鑳介噸澶嶏紝寤鸿閫氳繃鏁版嵁搴搒equence鐢熸垚锛�
        String outTradeNo = "tradepay" + System.currentTimeMillis()
                + (long) (Math.random() * 10000000L);

        // (蹇呭～) 璁㈠崟鏍囬锛岀矖鐣ユ弿杩扮敤鎴风殑鏀粯鐩殑銆傚鈥渪xx鍝佺墝xxx闂ㄥ簵娑堣垂鈥�
        String subject = "xxx鍝佺墝xxx闂ㄥ簵褰撻潰浠樻秷璐�";

        // (蹇呭～) 璁㈠崟鎬婚噾棰濓紝鍗曚綅涓哄厓锛屼笉鑳借秴杩�1浜垮厓
        // 濡傛灉鍚屾椂浼犲叆浜嗐€愭墦鎶橀噾棰濄€�,銆愪笉鍙墦鎶橀噾棰濄€�,銆愯鍗曟€婚噾棰濄€戜笁鑰�,鍒欏繀椤绘弧瓒冲涓嬫潯浠�:銆愯鍗曟€婚噾棰濄€�=銆愭墦鎶橀噾棰濄€�+銆愪笉鍙墦鎶橀噾棰濄€�
        String totalAmount = "0.01";

        // (蹇呭～) 浠樻鏉＄爜锛岀敤鎴锋敮浠樺疂閽卞寘鎵嬫満app鐐瑰嚮鈥滀粯娆锯€濅骇鐢熺殑浠樻鏉＄爜
        String authCode = "鐢ㄦ埛鑷繁鐨勬敮浠樺疂浠樻鐮�"; // 鏉＄爜绀轰緥锛�286648048691290423
        // (鍙€夛紝鏍规嵁闇€瑕佸喅瀹氭槸鍚︿娇鐢�)
        // 璁㈠崟鍙墦鎶橀噾棰濓紝鍙互閰嶅悎鍟嗗骞冲彴閰嶇疆鎶樻墸娲诲姩锛屽鏋滆鍗曢儴鍒嗗晢鍝佸弬涓庢墦鎶橈紝鍙互灏嗛儴鍒嗗晢鍝佹€讳环濉啓鑷虫瀛楁锛岄粯璁ゅ叏閮ㄥ晢鍝佸彲鎵撴姌
        // 濡傛灉璇ュ€兼湭浼犲叆,浣嗕紶鍏ヤ簡銆愯鍗曟€婚噾棰濄€�,銆愪笉鍙墦鎶橀噾棰濄€� 鍒欒鍊奸粯璁や负銆愯鍗曟€婚噾棰濄€�-
        // 銆愪笉鍙墦鎶橀噾棰濄€�
        // String discountableAmount = "1.00"; //

        // (鍙€�) 璁㈠崟涓嶅彲鎵撴姌閲戦锛屽彲浠ラ厤鍚堝晢瀹跺钩鍙伴厤缃姌鎵ｆ椿鍔紝濡傛灉閰掓按涓嶅弬涓庢墦鎶橈紝鍒欏皢瀵瑰簲閲戦濉啓鑷虫瀛楁
        // 濡傛灉璇ュ€兼湭浼犲叆,浣嗕紶鍏ヤ簡銆愯鍗曟€婚噾棰濄€�,銆愭墦鎶橀噾棰濄€�,鍒欒鍊奸粯璁や负銆愯鍗曟€婚噾棰濄€�-銆愭墦鎶橀噾棰濄€�
        String undiscountableAmount = "0.0";

        // 鍗栧鏀粯瀹濊处鍙稩D锛岀敤浜庢敮鎸佷竴涓绾﹁处鍙蜂笅鏀寔鎵撴鍒颁笉鍚岀殑鏀舵璐﹀彿锛�(鎵撴鍒皊ellerId瀵瑰簲鐨勬敮浠樺疂璐﹀彿)
        // 濡傛灉璇ュ瓧娈典负绌猴紝鍒欓粯璁や负涓庢敮浠樺疂绛剧害鐨勫晢鎴风殑PID锛屼篃灏辨槸appid瀵瑰簲鐨凱ID
        String sellerId = "";

        // 璁㈠崟鎻忚堪锛屽彲浠ュ浜ゆ槗鎴栧晢鍝佽繘琛屼竴涓缁嗗湴鎻忚堪锛屾瘮濡傚～鍐�"璐拱鍟嗗搧3浠跺叡20.00鍏�"
        String body = "璐拱鍟嗗搧3浠跺叡20.00鍏�";

        // 鍟嗘埛鎿嶄綔鍛樼紪鍙凤紝娣诲姞姝ゅ弬鏁板彲浠ヤ负鍟嗘埛鎿嶄綔鍛樺仛閿€鍞粺璁�
        String operatorId = "test_operator_id";

        // (蹇呭～) 鍟嗘埛闂ㄥ簵缂栧彿锛岄€氳繃闂ㄥ簵鍙峰拰鍟嗗鍚庡彴鍙互閰嶇疆绮惧噯鍒伴棬搴楃殑鎶樻墸淇℃伅锛岃璇㈡敮浠樺疂鎶€鏈敮鎸�
        String storeId = "test_store_id";

        // 涓氬姟鎵╁睍鍙傛暟锛岀洰鍓嶅彲娣诲姞鐢辨敮浠樺疂鍒嗛厤鐨勭郴缁熷晢缂栧彿(閫氳繃setSysServiceProviderId鏂规硶)锛岃鎯呰鍜ㄨ鏀粯瀹濇妧鏈敮鎸�
        String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);

        // 鏀粯瓒呮椂锛岀嚎涓嬫壂鐮佷氦鏄撳畾涔変负5鍒嗛挓
        String timeoutExpress = "5m";

        // 鍟嗗搧鏄庣粏鍒楄〃锛岄渶濉啓璐拱鍟嗗搧璇︾粏淇℃伅锛�
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 鍒涘缓涓€涓晢鍝佷俊鎭紝鍙傛暟鍚箟鍒嗗埆涓哄晢鍝乮d锛堜娇鐢ㄥ浗鏍囷級銆佸悕绉般€佸崟浠凤紙鍗曚綅涓哄垎锛夈€佹暟閲忥紝濡傛灉闇€瑕佹坊鍔犲晢鍝佺被鍒紝璇﹁GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx闈㈠寘", 1000, 1);
        // 鍒涘缓濂戒竴涓晢鍝佸悗娣诲姞鑷冲晢鍝佹槑缁嗗垪琛�
        goodsDetailList.add(goods1);

        // 缁х画鍒涘缓骞舵坊鍔犵涓€鏉″晢鍝佷俊鎭紝鐢ㄦ埛璐拱鐨勪骇鍝佷负鈥滈粦浜虹墮鍒封€濓紝鍗曚环涓�5.00鍏冿紝璐拱浜嗕袱浠�
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx鐗欏埛", 500, 2);
        goodsDetailList.add(goods2);

        String appAuthToken = "搴旂敤鎺堟潈浠ょ墝";// 鏍规嵁鐪熷疄鍊煎～鍐�

        // 鍒涘缓鏉＄爜鏀粯璇锋眰builder锛岃缃姹傚弬鏁�
        AlipayTradePayRequestBuilder builder = new AlipayTradePayRequestBuilder()
                // .setAppAuthToken(appAuthToken)
                .setOutTradeNo(outTradeNo).setSubject(subject).setAuthCode(authCode)
                .setTotalAmount(totalAmount).setStoreId(storeId)
                .setUndiscountableAmount(undiscountableAmount).setBody(body).setOperatorId(operatorId)
                .setExtendParams(extendParams).setSellerId(sellerId)
                .setGoodsDetailList(goodsDetailList).setTimeoutExpress(timeoutExpress);

        // 璋冪敤tradePay鏂规硶鑾峰彇褰撻潰浠樺簲绛�
        AlipayF2FPayResult result = service.tradePay(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("鏀粯瀹濇敮浠樻垚鍔�: )");
                break;

            case FAILED:
                log.error("鏀粯瀹濇敮浠樺け璐�!!!");
                break;

            case UNKNOWN:
                log.error("绯荤粺寮傚父锛岃鍗曠姸鎬佹湭鐭�!!!");
                break;

            default:
                log.error("涓嶆敮鎸佺殑浜ゆ槗鐘舵€侊紝浜ゆ槗杩斿洖寮傚父!!!");
                break;
        }
    }

    // 娴嬭瘯褰撻潰浠�2.0鏌ヨ璁㈠崟
    public void test_trade_query() {
        // (蹇呭～) 鍟嗘埛璁㈠崟鍙凤紝閫氳繃姝ゅ晢鎴疯鍗曞彿鏌ヨ褰撻潰浠樼殑浜ゆ槗鐘舵€�
        String outTradeNo = "tradepay14817938139942440181";

        // 鍒涘缓鏌ヨ璇锋眰builder锛岃缃姹傚弬鏁�
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(outTradeNo);

        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("鏌ヨ杩斿洖璇ヨ鍗曟敮浠樻垚鍔�: )");

                AlipayTradeQueryResponse response = result.getResponse();
                dumpResponse(response);

                log.info(response.getTradeStatus());
                if (Utils.isListNotEmpty(response.getFundBillList())) {
                    for (TradeFundBill bill : response.getFundBillList()) {
                        log.info(bill.getFundChannel() + ":" + bill.getAmount());
                    }
                }
                break;

            case FAILED:
                log.error("鏌ヨ杩斿洖璇ヨ鍗曟敮浠樺け璐ユ垨琚叧闂�!!!");
                break;

            case UNKNOWN:
                log.error("绯荤粺寮傚父锛岃鍗曟敮浠樼姸鎬佹湭鐭�!!!");
                break;

            default:
                log.error("涓嶆敮鎸佺殑浜ゆ槗鐘舵€侊紝浜ゆ槗杩斿洖寮傚父!!!");
                break;
        }
    }

    // 娴嬭瘯褰撻潰浠�2.0閫€娆�
    public void test_trade_refund() {
        // (蹇呭～) 澶栭儴璁㈠崟鍙凤紝闇€瑕侀€€娆句氦鏄撶殑鍟嗘埛澶栭儴璁㈠崟鍙�
        String outTradeNo = "tradepay14817938139942440181";

        // (蹇呭～) 閫€娆鹃噾棰濓紝璇ラ噾棰濆繀椤诲皬浜庣瓑浜庤鍗曠殑鏀粯閲戦锛屽崟浣嶄负鍏�
        String refundAmount = "0.01";

        // (鍙€夛紝闇€瑕佹敮鎸侀噸澶嶉€€璐ф椂蹇呭～)
        // 鍟嗘埛閫€娆捐姹傚彿锛岀浉鍚屾敮浠樺疂浜ゆ槗鍙蜂笅鐨勪笉鍚岄€€娆捐姹傚彿瀵瑰簲鍚屼竴绗斾氦鏄撶殑涓嶅悓閫€娆剧敵璇凤紝
        // 瀵逛簬鐩稿悓鏀粯瀹濅氦鏄撳彿涓嬪绗旂浉鍚屽晢鎴烽€€娆捐姹傚彿鐨勯€€娆句氦鏄擄紝鏀粯瀹濆彧浼氳繘琛屼竴娆￠€€娆�
        String outRequestNo = "";

        // (蹇呭～) 閫€娆惧師鍥狅紝鍙互璇存槑鐢ㄦ埛閫€娆惧師鍥狅紝鏂逛究涓哄晢瀹跺悗鍙版彁渚涚粺璁�
        String refundReason = "姝ｅ父閫€娆撅紝鐢ㄦ埛涔板浜�";

        // (蹇呭～) 鍟嗘埛闂ㄥ簵缂栧彿锛岄€€娆炬儏鍐典笅鍙互涓哄晢瀹跺悗鍙版彁渚涢€€娆炬潈闄愬垽瀹氬拰缁熻绛変綔鐢紝璇﹁鏀粯瀹濇妧鏈敮鎸�
        String storeId = "test_store_id";

        // 鍒涘缓閫€娆捐姹俠uilder锛岃缃姹傚弬鏁�
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason)
                .setOutRequestNo(outRequestNo).setStoreId(storeId);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("鏀粯瀹濋€€娆炬垚鍔�: )");
                break;

            case FAILED:
                log.error("鏀粯瀹濋€€娆惧け璐�!!!");
                break;

            case UNKNOWN:
                log.error("绯荤粺寮傚父锛岃鍗曢€€娆剧姸鎬佹湭鐭�!!!");
                break;

            default:
                log.error("涓嶆敮鎸佺殑浜ゆ槗鐘舵€侊紝浜ゆ槗杩斿洖寮傚父!!!");
                break;
        }
    }

    // 娴嬭瘯褰撻潰浠�2.0鐢熸垚鏀粯浜岀淮鐮�
    public void test_trade_precreate() {
        // (蹇呭～) 鍟嗘埛缃戠珯璁㈠崟绯荤粺涓敮涓€璁㈠崟鍙凤紝64涓瓧绗︿互鍐咃紝鍙兘鍖呭惈瀛楁瘝銆佹暟瀛椼€佷笅鍒掔嚎锛�
        // 闇€淇濊瘉鍟嗘埛绯荤粺绔笉鑳介噸澶嶏紝寤鸿閫氳繃鏁版嵁搴搒equence鐢熸垚锛�
        String outTradeNo = "tradeprecreate" + System.currentTimeMillis()
                + (long) (Math.random() * 10000000L);

        // (蹇呭～) 璁㈠崟鏍囬锛岀矖鐣ユ弿杩扮敤鎴风殑鏀粯鐩殑銆傚鈥渪xx鍝佺墝xxx闂ㄥ簵褰撻潰浠樻壂鐮佹秷璐光€�
        String subject = "xxx鍝佺墝xxx闂ㄥ簵褰撻潰浠樻壂鐮佹秷璐�";

        // (蹇呭～) 璁㈠崟鎬婚噾棰濓紝鍗曚綅涓哄厓锛屼笉鑳借秴杩�1浜垮厓
        // 濡傛灉鍚屾椂浼犲叆浜嗐€愭墦鎶橀噾棰濄€�,銆愪笉鍙墦鎶橀噾棰濄€�,銆愯鍗曟€婚噾棰濄€戜笁鑰�,鍒欏繀椤绘弧瓒冲涓嬫潯浠�:銆愯鍗曟€婚噾棰濄€�=銆愭墦鎶橀噾棰濄€�+銆愪笉鍙墦鎶橀噾棰濄€�
        String totalAmount = "0.01";

        // (鍙€�) 璁㈠崟涓嶅彲鎵撴姌閲戦锛屽彲浠ラ厤鍚堝晢瀹跺钩鍙伴厤缃姌鎵ｆ椿鍔紝濡傛灉閰掓按涓嶅弬涓庢墦鎶橈紝鍒欏皢瀵瑰簲閲戦濉啓鑷虫瀛楁
        // 濡傛灉璇ュ€兼湭浼犲叆,浣嗕紶鍏ヤ簡銆愯鍗曟€婚噾棰濄€�,銆愭墦鎶橀噾棰濄€�,鍒欒鍊奸粯璁や负銆愯鍗曟€婚噾棰濄€�-銆愭墦鎶橀噾棰濄€�
        String undiscountableAmount = "0";

        // 鍗栧鏀粯瀹濊处鍙稩D锛岀敤浜庢敮鎸佷竴涓绾﹁处鍙蜂笅鏀寔鎵撴鍒颁笉鍚岀殑鏀舵璐﹀彿锛�(鎵撴鍒皊ellerId瀵瑰簲鐨勬敮浠樺疂璐﹀彿)
        // 濡傛灉璇ュ瓧娈典负绌猴紝鍒欓粯璁や负涓庢敮浠樺疂绛剧害鐨勫晢鎴风殑PID锛屼篃灏辨槸appid瀵瑰簲鐨凱ID
        String sellerId = "";

        // 璁㈠崟鎻忚堪锛屽彲浠ュ浜ゆ槗鎴栧晢鍝佽繘琛屼竴涓缁嗗湴鎻忚堪锛屾瘮濡傚～鍐�"璐拱鍟嗗搧2浠跺叡15.00鍏�"
        String body = "璐拱鍟嗗搧3浠跺叡20.00鍏�";

        // 鍟嗘埛鎿嶄綔鍛樼紪鍙凤紝娣诲姞姝ゅ弬鏁板彲浠ヤ负鍟嗘埛鎿嶄綔鍛樺仛閿€鍞粺璁�
        String operatorId = "test_operator_id";

        // (蹇呭～) 鍟嗘埛闂ㄥ簵缂栧彿锛岄€氳繃闂ㄥ簵鍙峰拰鍟嗗鍚庡彴鍙互閰嶇疆绮惧噯鍒伴棬搴楃殑鎶樻墸淇℃伅锛岃璇㈡敮浠樺疂鎶€鏈敮鎸�
        String storeId = "test_store_id";

        // 涓氬姟鎵╁睍鍙傛暟锛岀洰鍓嶅彲娣诲姞鐢辨敮浠樺疂鍒嗛厤鐨勭郴缁熷晢缂栧彿(閫氳繃setSysServiceProviderId鏂规硶)锛岃鎯呰鍜ㄨ鏀粯瀹濇妧鏈敮鎸�
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 鏀粯瓒呮椂锛屽畾涔変负120鍒嗛挓
        String timeoutExpress = "120m";

        // 鍟嗗搧鏄庣粏鍒楄〃锛岄渶濉啓璐拱鍟嗗搧璇︾粏淇℃伅锛�
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 鍒涘缓涓€涓晢鍝佷俊鎭紝鍙傛暟鍚箟鍒嗗埆涓哄晢鍝乮d锛堜娇鐢ㄥ浗鏍囷級銆佸悕绉般€佸崟浠凤紙鍗曚綅涓哄垎锛夈€佹暟閲忥紝濡傛灉闇€瑕佹坊鍔犲晢鍝佺被鍒紝璇﹁GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx灏忛潰鍖�", 1000, 1);
        // 鍒涘缓濂戒竴涓晢鍝佸悗娣诲姞鑷冲晢鍝佹槑缁嗗垪琛�
        goodsDetailList.add(goods1);

        // 缁х画鍒涘缓骞舵坊鍔犵涓€鏉″晢鍝佷俊鎭紝鐢ㄦ埛璐拱鐨勪骇鍝佷负鈥滈粦浜虹墮鍒封€濓紝鍗曚环涓�5.00鍏冿紝璐拱浜嗕袱浠�
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx鐗欏埛", 500, 2);
        goodsDetailList.add(goods2);

        // 鍒涘缓鎵爜鏀粯璇锋眰builder锛岃缃姹傚弬鏁�
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                // .setNotifyUrl("http://www.test-notify-url.com")//鏀粯瀹濇湇鍔″櫒涓诲姩閫氱煡鍟嗘埛鏈嶅姟鍣ㄩ噷鎸囧畾鐨勯〉闈ttp璺緞,鏍规嵁闇€瑕佽缃�
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("鏀粯瀹濋涓嬪崟鎴愬姛: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 闇€瑕佷慨鏀逛负杩愯鏈哄櫒涓婄殑璺緞
                String filePath = String.format("/Users/sudo/Desktop/qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + filePath);
                // ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
                log.error("鏀粯瀹濋涓嬪崟澶辫触!!!");
                break;

            case UNKNOWN:
                log.error("绯荤粺寮傚父锛岄涓嬪崟鐘舵€佹湭鐭�!!!");
                break;

            default:
                log.error("涓嶆敮鎸佺殑浜ゆ槗鐘舵€侊紝浜ゆ槗杩斿洖寮傚父!!!");
                break;
        }
    }
}
