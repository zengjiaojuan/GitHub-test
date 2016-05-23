package com.llpay.client.config;

/**
* 商户配置信息
* @author guoyx e-mail:guoyx@lianlian.com
* @date:2013-6-25 下午01:45:40
* @version :1.0
*
*/
public interface PartnerConfig {
    // 银通公钥
    String YT_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
    // 商户私钥
    String TRADER_PRI_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMmR9VIAhU1LWoqJFgi9zGWUO8N/zphH6hjtQWVP4rc/EKCu/EstgHkPocfhmo6jFeaAkNKDRw2Ex3u7CScxk8Be6yyMUjU3AZFuKCe0XpJuwlVthF1G1Accfu9nVe8IPyR0PaKoAdBeequSrHVdMEfs6VvBTFGxnhri8TcXXBPdAgMBAAECgYB3M12D1NL/1qQAcPkaykLpfzOodRhTbZO+ke+uXgOQojbPDywbuRjUfq4JahzgZQk6eqUqGIAYOoFNZloPV34rouwJt8TG7o/pdqOt1E5BK79UzXbGBFqcLVREAkSvizu0WvoGQip8T6zS0aFviukFsFyl9dqi00XSSOoA5eLc8QJBAPtEa95WbjyV9mXRaWzD9XcejzpwX7IwujU+vZC5FQPYxlS4YKil5ptmzh+zokSk4UaAi3Vu2KHFBFoRZtUeLRcCQQDNXedcvyXjrcq0+IU1emU0TPmp7P7ayI0wEOraVWU1EtnxqcOzT/sHDZzv/3LdntEllo+L7xC1vuTRPc9cdCcrAkEAyqYMOW2LyuFHsCaM2GPS/RdwfLlU9OnHxFmtmaMsB8Y1bu6lRl9G9d6cL4U5QhOYfLv/vd6AaTf0oo+WWBvWUQJAOL14HeTmGAi9V9DOJR9i9NdLp4pGQ7ZjY+NYr+gmJUrHEoDgBBvV9HXrZVfDqb2mdWcq0/PCPyS13aXhM98XrwJAZCtiHCIGi18Wmn0opQ7lXHUcNGalgFZu3RamMn7Bq80IQ3AtS4+no5Zodj+O3WSY29uSG1KV9KUfuF6TmovGJQ==";
    // 商户编号
    String OID_PARTNER = "201510191000543502";
    // 签名方式 RSA或MD5
    String SIGN_TYPE = "RSA";
    // 接口版本号，固定1.0
    String VERSION = "1.0";
    // 业务类型，连连支付根据商户业务为商户开设的业务类型； （101001：虚拟商品销售、109001：实物商品销售、108001：外部账户充值）
    String BUSI_PARTNER = "108001";
}
