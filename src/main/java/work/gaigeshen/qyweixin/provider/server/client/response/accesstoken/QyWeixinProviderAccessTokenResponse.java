package work.gaigeshen.qyweixin.provider.server.client.response.accesstoken;

import work.gaigeshen.qyweixin.provider.server.client.response.QyWeixinResponse;

/**
 *
 * @author gaigeshen
 */
public class QyWeixinProviderAccessTokenResponse extends QyWeixinResponse {

    public String provider_access_token;

    public Long expires_in;
}
