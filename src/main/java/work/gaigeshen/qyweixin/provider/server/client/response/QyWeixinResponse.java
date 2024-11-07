package work.gaigeshen.qyweixin.provider.server.client.response;

import work.gaigeshen.tripartite.core.client.response.ClientResponse;

/**
 * 所有的钉钉新版接口客户端响应都需要继承此类
 *
 * @author gaigeshen
 */
public abstract class QyWeixinResponse implements ClientResponse {

    public Integer errcode;

    public String errmsg;
}
