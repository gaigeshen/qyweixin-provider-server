package work.gaigeshen.qyweixin.provider.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import work.gaigeshen.qyweixin.provider.server.client.DefaultQyWeixinProviderClient;
import work.gaigeshen.qyweixin.provider.server.client.QyWeixinProviderClient;
import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.client.suite.QyWeixinSuiteTicketStore;
import work.gaigeshen.qyweixin.provider.server.notify.QyWeixinNotifyContentFilter;
import work.gaigeshen.qyweixin.provider.server.notify.QyWeixinNotifyContentReceiver;
import work.gaigeshen.qyweixin.provider.server.notify.message.notify.QyWeixinCancelAuthMessageNotifyContentProcessor;
import work.gaigeshen.qyweixin.provider.server.notify.message.notify.QyWeixinCreateAuthMessageNotifyContentProcessor;
import work.gaigeshen.qyweixin.provider.server.notify.message.notify.QyWeixinMessageNotifyContentProcessor;
import work.gaigeshen.qyweixin.provider.server.notify.message.notify.QyWeixinSuiteTicketMessageNotifyContentProcessor;
import work.gaigeshen.qyweixin.provider.server.persistence.ProviderAccessTokenStore;
import work.gaigeshen.qyweixin.provider.server.persistence.SuiteAccessTokenStore;
import work.gaigeshen.qyweixin.provider.server.persistence.SuiteTicketStore;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.PermanentCodeRepository;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.ProviderAccessTokenRepository;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.SuiteAccessTokenRepository;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.SuiteTicketRepository;
import work.gaigeshen.tripartite.core.client.ClientException;
import work.gaigeshen.tripartite.core.client.accesstoken.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 企业微信自动配置
 *
 * @author gaigeshen
 */
@EnableConfigurationProperties({QyWeixinProperties.class})
@Configuration
@RequiredArgsConstructor
@Slf4j
public class QyWeixinConfiguration {

    private final QyWeixinProperties properties;

    private final ProviderAccessTokenRepository providerAccessTokenRepository;

    private final SuiteAccessTokenRepository suiteAccessTokenRepository;

    private final SuiteTicketRepository suiteTicketRepository;

    private final PermanentCodeRepository permanentCodeRepository;

    @Bean
    public QyWeixinNotifyContentReceiver notifyContentReceiver(QyWeixinProviderClient providerClient,
                                                               List<QyWeixinMessageNotifyContentProcessor> processors) {
        QyWeixinNotifyContentReceiver receiver = new QyWeixinNotifyContentReceiver(providerClient);
        receiver.setProcessors(new ArrayList<>(processors));
        return receiver;
    }

    @Bean
    public FilterRegistrationBean<?> notifyContentFilter(QyWeixinNotifyContentReceiver receiver, QyWeixinProviderClient providerClient) {
        QyWeixinNotifyContentFilter filter = new QyWeixinNotifyContentFilter(receiver, providerClient);
        FilterRegistrationBean<QyWeixinNotifyContentFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setUrlPatterns(Collections.singletonList("/notify-receiver"));
        filterBean.setFilter(filter);
        return filterBean;
    }

    @Bean(initMethod = "init")
    public QyWeixinProviderClient providerClient() {
        if (StringUtils.isAnyBlank(properties.getServerHost(), properties.getCorpId(), properties.getProviderSecret())) {
            throw new IllegalStateException("serverHost, corpId and providerSecret cannot be blank");
        }
        if (StringUtils.isAnyBlank(properties.getSuiteId(), properties.getSuiteSecret())) {
            throw new IllegalStateException("suiteId and suiteSecret cannot be blank");
        }
        if (StringUtils.isAnyBlank(properties.getToken(), properties.getAesKey())) {
            throw new IllegalStateException("token and aesKey cannot be blank");
        }
        QyWeixinConfig qyWeixinConfig = QyWeixinConfig.builder()
                .setServerHost(properties.getServerHost()).setCorpId(properties.getCorpId())
                .setProviderSecret(properties.getProviderSecret())
                .setSuiteId(properties.getSuiteId()).setSuiteSecret(properties.getSuiteSecret())
                .setToken(properties.getToken()).setAesKey(properties.getAesKey())
                .build();
        return new DefaultQyWeixinProviderClient(qyWeixinConfig, accessTokenManager(), suiteAccessTokenManager(), suiteTicketStore());
    }

    @Bean(destroyMethod = "shutdown")
    public AccessTokenManager<QyWeixinConfig> accessTokenManager() {
        return new DefaultAccessTokenManager<>(accessTokenStore(), accessTokenRefresher());
    }

    @Bean(destroyMethod = "shutdown")
    public AccessTokenManager<QyWeixinConfig> suiteAccessTokenManager() {
        return new DefaultAccessTokenManager<>(suiteAccessTokenStore(), suiteAccessTokenRefresher());
    }

    @Bean
    public AccessTokenRefresher<QyWeixinConfig> accessTokenRefresher() {
        return (config, oat) -> {
            try {
                return ((DefaultQyWeixinProviderClient) providerClient()).getNewAccessToken();
            } catch (ClientException e) {
                throw new AccessTokenRefreshException(e.getMessage(), e).setCurrentAccessToken(oat).setCanRetry(true);
            }
        };
    }

    @Bean
    public AccessTokenRefresher<QyWeixinConfig> suiteAccessTokenRefresher() {
        return (config, oat) -> {
            try {
                return ((DefaultQyWeixinProviderClient) providerClient()).getNewSuiteAccessToken();
            } catch (ClientException e) {
                throw new AccessTokenRefreshException(e.getMessage(), e).setCurrentAccessToken(oat).setCanRetry(true);
            }
        };
    }

    @Bean
    public AccessTokenStore<QyWeixinConfig> accessTokenStore() {
        return new ProviderAccessTokenStore(providerAccessTokenRepository);
    }

    @Bean
    public AccessTokenStore<QyWeixinConfig> suiteAccessTokenStore() {
        return new SuiteAccessTokenStore(suiteAccessTokenRepository);
    }

    @Bean
    public QyWeixinSuiteTicketStore<QyWeixinConfig> suiteTicketStore() {
        return new SuiteTicketStore(suiteTicketRepository);
    }

    @Bean
    public QyWeixinSuiteTicketMessageNotifyContentProcessor suiteTicketMessageNotifyContentProcessor() {
        return new QyWeixinSuiteTicketMessageNotifyContentProcessor(providerClient(), messageNotifyContentProcessorExecutorService());
    }

    @Bean
    public QyWeixinCreateAuthMessageNotifyContentProcessor createAuthMessageNotifyContentProcessor() {
        return new QyWeixinCreateAuthMessageNotifyContentProcessor(providerClient(), permanentCodeRepository, messageNotifyContentProcessorExecutorService());
    }

    @Bean
    public QyWeixinCancelAuthMessageNotifyContentProcessor cancelAuthMessageNotifyContentProcessor() {
        return new QyWeixinCancelAuthMessageNotifyContentProcessor(permanentCodeRepository, messageNotifyContentProcessorExecutorService());
    }

    @Bean
    public Executor messageNotifyContentProcessorExecutorService() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setQueueCapacity(10);
        threadPoolTaskExecutor.setThreadNamePrefix("message-notify-processor-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
