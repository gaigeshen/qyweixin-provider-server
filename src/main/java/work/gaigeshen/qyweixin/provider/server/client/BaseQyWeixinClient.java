package work.gaigeshen.qyweixin.provider.server.client;

import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.client.suite.QyWeixinSuiteTicket;
import work.gaigeshen.tripartite.core.client.Client;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessToken;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenManager;

import java.util.Objects;

/**
 * 企业微信客户端
 *
 * @author gaigeshen
 */
public interface BaseQyWeixinClient extends Client<QyWeixinConfig> {

    default String getSuiteAccessTokenValue() {
        AccessToken accessToken = getSuiteAccessToken();
        return Objects.isNull(accessToken) ? "" : accessToken.getAccessToken();
    }

    default AccessToken getSuiteAccessToken() {
        AccessTokenManager<QyWeixinConfig> accessTokenManager = getSuiteAccessTokenManager();
        if (Objects.isNull(accessTokenManager)) {
            return null;
        }
        return accessTokenManager.findAccessToken(getConfig());
    }

    AccessTokenManager<QyWeixinConfig> getSuiteAccessTokenManager();

    void setNewSuiteTicket(QyWeixinSuiteTicket suiteTicket);
}
