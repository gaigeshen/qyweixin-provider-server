package work.gaigeshen.qyweixin.provider.server.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.persistence.model.ProviderAccessToken;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.ProviderAccessTokenRepository;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessToken;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenStore;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenStoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ProviderAccessTokenStore implements AccessTokenStore<QyWeixinConfig> {

    private final ProviderAccessTokenRepository accessTokenRepository;

    @Transactional
    @Override
    public boolean save(QyWeixinConfig config, AccessToken accessToken) throws AccessTokenStoreException {
        Optional<ProviderAccessToken> existsAccessTokenOptional = findAccessToken(config);
        if (existsAccessTokenOptional.isPresent()) {
            ProviderAccessToken existsAccessToken = existsAccessTokenOptional.get();
            existsAccessToken.setConfigId(buildConfigId(config));
            existsAccessToken.setConfig(QyWeixinConfig.serialize(config));
            BeanUtils.copyProperties(accessToken, existsAccessToken);
            accessTokenRepository.save(existsAccessToken);
            return false;
        }
        ProviderAccessToken newAccessToken = new ProviderAccessToken();
        newAccessToken.setConfigId(buildConfigId(config));
        newAccessToken.setConfig(QyWeixinConfig.serialize(config));
        BeanUtils.copyProperties(accessToken, newAccessToken);
        accessTokenRepository.save(newAccessToken);
        return true;
    }

    @Transactional
    @Override
    public void delete(QyWeixinConfig config) throws AccessTokenStoreException {
        findAccessToken(config).ifPresent(accessTokenRepository::delete);
    }

    @Override
    public AccessToken find(QyWeixinConfig config) throws AccessTokenStoreException {
        return findAccessToken(config).map(accessToken ->
                    AccessToken.builder()
                        .setAccessToken(accessToken.getAccessToken())
                        .setExpiresIn(accessToken.getExpiresIn())
                        .setExpiresTimestamp(accessToken.getExpiresTimestamp())
                        .setUpdateTime(accessToken.getUpdateTime())
                        .build())
                .orElse(null);
    }

    @Override
    public Map<QyWeixinConfig, AccessToken> findAll() throws AccessTokenStoreException {
        Map<QyWeixinConfig, AccessToken> allAccessTokens = new HashMap<>();
        for (ProviderAccessToken providerAccessToken : accessTokenRepository.findAll()) {
            QyWeixinConfig config = QyWeixinConfig.deserialize(providerAccessToken.getConfig());
            AccessToken accessToken = AccessToken.builder()
                    .setAccessToken(providerAccessToken.getAccessToken())
                    .setExpiresIn(providerAccessToken.getExpiresIn())
                    .setExpiresTimestamp(providerAccessToken.getExpiresTimestamp())
                    .setUpdateTime(providerAccessToken.getUpdateTime())
                    .build();
            allAccessTokens.put(config, accessToken);
        }
        return allAccessTokens;
    }

    private Optional<ProviderAccessToken> findAccessToken(QyWeixinConfig config) {
        return accessTokenRepository.findOne((root, query, criteriaBuilder) -> {
            String configId = buildConfigId(config);
            return criteriaBuilder.equal(root.get("configId"), configId);
        });
    }

    private String buildConfigId(QyWeixinConfig config) {
        return config.getCorpId();
    }
}
