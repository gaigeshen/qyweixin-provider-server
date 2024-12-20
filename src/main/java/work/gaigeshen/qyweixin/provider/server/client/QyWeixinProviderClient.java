package work.gaigeshen.qyweixin.provider.server.client;

import work.gaigeshen.qyweixin.provider.server.client.parameters.auth.QyWeixinAuthInfoGetParameters;
import work.gaigeshen.qyweixin.provider.server.client.parameters.auth.QyWeixinPermanentCodeGetParameters;
import work.gaigeshen.qyweixin.provider.server.client.response.auth.QyWeixinAuthInfoGetResponse;
import work.gaigeshen.qyweixin.provider.server.client.response.auth.QyWeixinPermanentCodeGetResponse;
import work.gaigeshen.tripartite.core.client.ClientException;

/**
 * 企业微信服务商接口客户端
 *
 * @author gaigeshen
 */
public interface QyWeixinProviderClient extends BaseQyWeixinClient {

    /**
     * 代开发应用永久授权码获取
     *
     * @param parameters 请求参数不能为空
     * @return 响应结果不为空
     * @throws ClientException 执行请求的时候发生异常
     * @see <a href="https://developer.work.weixin.qq.com/document/path/97163">接口文档</a>
     */
    default QyWeixinPermanentCodeGetResponse permanentCodeGet(QyWeixinPermanentCodeGetParameters parameters) throws ClientException {
        return execute(parameters, QyWeixinPermanentCodeGetResponse.class, "/cgi-bin/service/get_permanent_code?suite_access_token={suite_access_token}", getSuiteAccessTokenValue());
    }

    /**
     * 获取企业授权信息
     *
     * @param parameters 请求参数不能为空
     * @return 响应结果不为空
     * @throws ClientException 执行请求的时候发生异常
     * @see <a href="https://developer.work.weixin.qq.com/document/path/97171">接口文档</a>
     */
    default QyWeixinAuthInfoGetResponse authInfoGet(QyWeixinAuthInfoGetParameters parameters) throws ClientException {
        return execute(parameters, QyWeixinAuthInfoGetResponse.class, "/cgi-bin/service/get_auth_info?suite_access_token={suite_access_token}", getSuiteAccessTokenValue());
    }
}
