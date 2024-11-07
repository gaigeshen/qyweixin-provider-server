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
public class QyWeixinSuiteAccessTokenParameters extends QyWeixinParameters {

    public String suite_id;

    public String suite_secret;

    public String suite_ticket;
}
