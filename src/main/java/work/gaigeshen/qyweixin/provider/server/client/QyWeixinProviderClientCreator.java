package work.gaigeshen.qyweixin.provider.server.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.client.suite.QyWeixinSuiteTicketStore;
import work.gaigeshen.tripartite.core.client.Client;
import work.gaigeshen.tripartite.core.client.ClientCreationException;
import work.gaigeshen.tripartite.core.client.ClientCreator;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenManager;

/**
 *
 * @author gaigeshen
 */
@RequiredArgsConstructor
@Slf4j
public class QyWeixinProviderClientCreator implements ClientCreator<QyWeixinConfig> {

    private final AccessTokenManager<QyWeixinConfig> accessTokenManager;

    private final AccessTokenManager<QyWeixinConfig> suiteAccessTokenManager;

    private final QyWeixinSuiteTicketStore<QyWeixinConfig> suiteTicketStore;

    @Override
    public Client<QyWeixinConfig> create(QyWeixinConfig config) throws ClientCreationException {
        log.info("creating qyweixin provider client: {}", config);
        Client<QyWeixinConfig> qyWeixinClient;
        try {
            qyWeixinClient = new DefaultQyWeixinProviderClient(config, accessTokenManager, suiteAccessTokenManager, suiteTicketStore);
            qyWeixinClient.init();
        }
        catch (Exception e) {
            throw new ClientCreationException(e.getMessage(), e);
        }
        return qyWeixinClient;
    }
}
