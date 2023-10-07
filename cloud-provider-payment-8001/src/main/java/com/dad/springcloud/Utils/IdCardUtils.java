package com.dad.springcloud.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author gmq
 * @Date 2023-09-15
 * @Description
 **/
@Slf4j
public class IdCardUtils {


    public static String setBrithDay(String idCard) {
        String brithDay=idCard.substring(6, 14);
       return brithDay;
    }


    public static Integer setSexNum(String  idCard) {
        String sexNum=idCard.substring(16, 17);
        return Integer.parseInt(sexNum);
    }

    public static void main(String[] args) {
        /*身份证号码是由18位数字组成的，他们分别表示：
        1、前1、2位数字表示：所在省份的代码。
        2、前3、4位数字表示：所在城市的代码。
        3、前5、6位数字表示：所在区县的代码。
        4、第7～14位数字表示：出生年、月、日，7、8、9、10位是年，11、12位是月，13、14位是日。
        5、第15、16位数字表示：所在地的派出所的代码。
        6、第17位数字表示性别：奇数表示男性，偶数表示女性。
        7、第18位数字是校检码：校检码可以是0～9的数字，有时也用X表示。
        8、*尾号X是作为尾号的校检码，是由号码编制单位。X是罗马数字的10，
        用X来代替10，可以保证公民身份证符合国家标准。*/
        String idCard = "370883199104082530";
        String birthDate = idCard.substring(6, 14);
        birthDate=birthDate.substring(0,4)+"/"+birthDate.substring(4,6)+"/"+birthDate.substring(6,8);
        log.info("------birthDate---" + birthDate);
        String sex = idCard.substring(16, 17);
        if (Integer.parseInt(sex) % 2 == 0) {
            log.info("------sex--女-" + sex);
        } else {
            log.info("------sex--男-" + sex);
        }
        //例如，中国银行北京分行的SWIFT代码是 BKCHCNBJ。
        // 其中，“BKCH”是银行代码，“CN”是国家代码（代表中国），“BJ”是地区代码（代表北京）。

        String swiftCode = "BKCHCNBJ";
        String countryCode = swiftCode.substring(4, 6);
        log.info("------countryCode---" + countryCode);
    }

}