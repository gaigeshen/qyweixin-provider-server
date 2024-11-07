package work.gaigeshen.qyweixin.provider.server.client.response.auth;

import work.gaigeshen.qyweixin.provider.server.client.response.QyWeixinResponse;

import java.util.Collection;

/**
 *
 * @author gaigeshen
 */
public class QyWeixinPermanentCodeGetResponse extends QyWeixinResponse {

    public String permanent_code;

    public AuthCorpInfo auth_corp_info;

    public AuthInfo auth_info;

    public AuthUserInfo auth_user_info;

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

    public static class AuthUserInfo {

        public String avatar;

        public String name;

        public String open_userid;

        public String userid;
    }
}
