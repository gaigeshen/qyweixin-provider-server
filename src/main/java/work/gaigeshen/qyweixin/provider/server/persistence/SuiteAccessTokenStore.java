package work.gaigeshen.qyweixin.provider.server.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.persistence.model.SuiteAccessToken;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.SuiteAccessTokenRepository;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessToken;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenStore;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenStoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SuiteAccessTokenStore implements AccessTokenStore<QyWeixinConfig> {

    private final SuiteAccessTokenRepository accessTokenRepository;

    @Transactional
    @Override
    public boolean save(QyWeixinConfig config, AccessToken accessToken) throws AccessTokenStoreException {
        Optional<SuiteAccessToken> existsAccessTokenOptional = findAccessToken(config);
        if (existsAccessTokenOptional.isPresent()) {
            SuiteAccessToken existsAccessToken = existsAccessTokenOptional.get();
            existsAccessToken.setConfigId(buildConfigId(config));
            existsAccessToken.setConfig(QyWeixinConfig.serialize(config));
            BeanUtils.copyProperties(accessToken, existsAccessToken);
            accessTokenRepository.save(existsAccessToken);
            return false;
        }
        SuiteAccessToken newAccessToken = new SuiteAccessToken();
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
        for (SuiteAccessToken suiteAccessToken : accessTokenRepository.findAll()) {
            QyWeixinConfig config = QyWeixinConfig.deserialize(suiteAccessToken.getConfig());
            AccessToken accessToken = AccessToken.builder()
                    .setAccessToken(suiteAccessToken.getAccessToken())
                    .setExpiresIn(suiteAccessToken.getExpiresIn())
                    .setExpiresTimestamp(suiteAccessToken.getExpiresTimestamp())
                    .setUpdateTime(suiteAccessToken.getUpdateTime())
                    .build();
            allAccessTokens.put(config, accessToken);
        }
        return allAccessTokens;
    }

    private Optional<SuiteAccessToken> findAccessToken(QyWeixinConfig config) {
        return accessTokenRepository.findOne((root, query, criteriaBuilder) -> {
            String configId = buildConfigId(config);
            return criteriaBuilder.equal(root.get("configId"), configId);
        });
    }

    private String buildConfigId(QyWeixinConfig config) {
        return String.format("%s-%s", config.getCorpId(), config.getSuiteId());
    }
}
