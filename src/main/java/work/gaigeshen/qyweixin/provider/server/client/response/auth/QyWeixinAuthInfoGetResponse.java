package work.gaigeshen.qyweixin.provider.server.client.response.auth;

import work.gaigeshen.qyweixin.provider.server.client.response.QyWeixinResponse;

import java.util.Collection;

/**
 *
 * @author gaigeshen
 */
public class QyWeixinAuthInfoGetResponse extends QyWeixinResponse {

    public AuthCorpInfo auth_corp_info;

    public AuthInfo auth_info;

    public static class AuthCorpInfo {

        public String corpid;

        public String corp_name;
    }

    public static class AuthInfo {

        public Collection<AuthInfoAgent> agent;
    }

    public static class AuthInfoAgent {

        public Integer agentid;

        public String name;
    }
}
