package work.gaigeshen.qyweixin.provider.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 企业微信配置信息
 *
 * @author gaigeshen
 */
@ConfigurationProperties("spring.qyweixin")
@Data
public class QyWeixinProperties {

    /**
     * 服务器的地址
     */
    private String serverHost;

    /**
     * 企业编号
     */
    private String corpId;

    /**
     * 服务商的凭证密钥
     */
    private String providerSecret;

    /**
     * 代开发应用模板标识
     */
    private String suiteId;

    /**
     * 代开发应用模板凭证密钥
     */
    private String suiteSecret;

    /**
     * 用于校验回调签名
     */
    private String token;

    /**
     * 用于加解密回调内容
     */
    private String aesKey;
}
