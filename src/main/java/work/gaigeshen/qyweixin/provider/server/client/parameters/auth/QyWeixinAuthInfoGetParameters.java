package work.gaigeshen.qyweixin.provider.server.client.parameters.auth;

import work.gaigeshen.qyweixin.provider.server.client.parameters.QyWeixinParameters;
import work.gaigeshen.tripartite.core.parameter.converter.JsonParametersConverter;
import work.gaigeshen.tripartite.core.parameter.converter.Parameters;

/**
 *
 * @author gaigeshen
 */
@Parameters(
        converter = JsonParametersConverter.class
)
public class QyWeixinAuthInfoGetParameters extends QyWeixinParameters {

    public String auth_corpid;

    public String permanent_code;
}
