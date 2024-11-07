package work.gaigeshen.qyweixin.provider.server.client.config;

import lombok.Getter;
import work.gaigeshen.tripartite.core.client.config.Config;
import work.gaigeshen.tripartite.core.util.json.JsonCodec;

import java.util.Map;
import java.util.Objects;

/**
 * 企业微信配置信息
 *
 * @author gaigeshen
 */
@Getter
public class QyWeixinConfig implements Config {

    /**
     * 服务器的地址
     */
    private final String serverHost;

    /**
     * 企业编号
     */
    private final String corpId;

    /**
     * 服务商的凭证密钥
     */
    private final String providerSecret;

    /**
     * 代开发应用模板标识
     */
    private final String suiteId;

    /**
     * 代开发应用模板凭证密钥
     */
    private final String suiteSecret;

    /**
     * 用于校验回调签名
     */
    private final String token;

    /**
     * 用于加解密回调内容
     */
    private final String aesKey;

    private QyWeixinConfig(Builder builder) {
        this.serverHost = builder.serverHost;
        this.corpId = builder.corpId;
        this.providerSecret = builder.providerSecret;
        this.suiteId = builder.suiteId;
        this.suiteSecret = builder.suiteSecret;
        this.token = builder.token;
        this.aesKey = builder.aesKey;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static String serialize(QyWeixinConfig config) {
        return JsonCodec.instance().encode(config);
    }

    public static QyWeixinConfig deserialize(String serialized) {
        Map<String, Object> decodedObject = JsonCodec.instance().decodeObject(serialized);
        return QyWeixinConfig.builder()
                .setServerHost((String) decodedObject.get("serverHost"))
                .setCorpId((String) decodedObject.get("corpId"))
                .setProviderSecret((String) decodedObject.get("providerSecret"))
                .setSuiteId((String) decodedObject.get("suiteId"))
                .setSuiteSecret((String) decodedObject.get("suiteSecret"))
                .setToken((String) decodedObject.get("token"))
                .setAesKey((String) decodedObject.get("aesKey"))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QyWeixinConfig that = (QyWeixinConfig) o;
        return Objects.equals(corpId, that.corpId) && Objects.equals(suiteId, that.suiteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corpId, suiteId);
    }

    @Override
    public String toString() {
        return "QyWeixinConfig: " + corpId + ", " + suiteId;
    }

    /**
     * @author gaigeshen
     */
    public static class Builder {

        private String serverHost;

        private String corpId;

        private String providerSecret;

        private String suiteId;

        private String suiteSecret;

        private String token;

        private String aesKey;

        public Builder setServerHost(String serverHost) {
            this.serverHost = serverHost;
            return this;
        }

        public Builder setCorpId(String corpId) {
            this.corpId = corpId;
            return this;
        }

        public Builder setProviderSecret(String providerSecret) {
            this.providerSecret = providerSecret;
            return this;
        }

        public Builder setSuiteId(String suiteId) {
            this.suiteId = suiteId;
            return this;
        }

        public Builder setSuiteSecret(String suiteSecret) {
            this.suiteSecret = suiteSecret;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setAesKey(String aesKey) {
            this.aesKey = aesKey;
            return this;
        }

        public QyWeixinConfig build() {
            return new QyWeixinConfig(this);
        }
    }
}
