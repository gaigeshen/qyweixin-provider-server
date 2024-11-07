package work.gaigeshen.qyweixin.provider.server.client.parameters.accesstoken;

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
public class QyWeixinProviderAccessTokenParameters extends QyWeixinParameters {

    public String corpid;

    public String provider_secret;
}
