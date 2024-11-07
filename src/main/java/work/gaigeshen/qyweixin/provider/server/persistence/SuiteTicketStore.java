package work.gaigeshen.qyweixin.provider.server.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.client.suite.QyWeixinSuiteTicket;
import work.gaigeshen.qyweixin.provider.server.client.suite.QyWeixinSuiteTicketStore;
import work.gaigeshen.qyweixin.provider.server.persistence.model.SuiteTicket;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.SuiteTicketRepository;
import work.gaigeshen.tripartite.core.client.accesstoken.AccessTokenStoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SuiteTicketStore implements QyWeixinSuiteTicketStore<QyWeixinConfig> {

    private final SuiteTicketRepository suiteTicketRepository;

    @Transactional
    @Override
    public boolean save(QyWeixinConfig config, QyWeixinSuiteTicket suiteTicket) throws AccessTokenStoreException {
        Optional<SuiteTicket> existsSuiteTicketOptional = findSuiteTicket(config);
        if (existsSuiteTicketOptional.isPresent()) {
            SuiteTicket existsSuiteTicket = existsSuiteTicketOptional.get();
            existsSuiteTicket.setConfigId(buildConfigId(config));
            existsSuiteTicket.setConfig(QyWeixinConfig.serialize(config));
            existsSuiteTicket.setSuiteId(suiteTicket.getSuiteId());
            existsSuiteTicket.setSuiteTicket(suiteTicket.getTicket());
            existsSuiteTicket.setUpdateTime(new Date());
            suiteTicketRepository.save(existsSuiteTicket);
            return false;
        }
        SuiteTicket newSuiteTicket = new SuiteTicket();
        newSuiteTicket.setConfigId(buildConfigId(config));
        newSuiteTicket.setConfig(QyWeixinConfig.serialize(config));
        newSuiteTicket.setSuiteId(suiteTicket.getSuiteId());
        newSuiteTicket.setSuiteTicket(suiteTicket.getTicket());
        newSuiteTicket.setUpdateTime(new Date());
        suiteTicketRepository.save(newSuiteTicket);
        return true;
    }

    @Override
    public QyWeixinSuiteTicket find(QyWeixinConfig config) throws AccessTokenStoreException {
        return findSuiteTicket(config).map(suiteTicket ->
                QyWeixinSuiteTicket.builder()
                        .setSuiteId(suiteTicket.getSuiteId())
                        .setTicket(suiteTicket.getSuiteTicket())
                        .build()
                ).orElse(null);
    }

    @Override
    public Map<QyWeixinConfig, QyWeixinSuiteTicket> findAll() throws AccessTokenStoreException {
        Map<QyWeixinConfig, QyWeixinSuiteTicket> allSuiteTickets = new HashMap<>();
        for (SuiteTicket suiteTicket : suiteTicketRepository.findAll()) {
            QyWeixinConfig config = QyWeixinConfig.deserialize(suiteTicket.getConfig());
            QyWeixinSuiteTicket qyWeixinSuiteTicket = QyWeixinSuiteTicket.builder()
                    .setSuiteId(suiteTicket.getSuiteId())
                    .setTicket(suiteTicket.getSuiteTicket())
                    .build();
            allSuiteTickets.put(config, qyWeixinSuiteTicket);
        }
        return allSuiteTickets;
    }

    private Optional<SuiteTicket> findSuiteTicket(QyWeixinConfig config) {
        return suiteTicketRepository.findOne((root, query, criteriaBuilder) -> {
            String configId = buildConfigId(config);
            return criteriaBuilder.equal(root.get("configId"), configId);
        });
    }

    private String buildConfigId(QyWeixinConfig config) {
        return String.format("%s-%s", config.getCorpId(), config.getSuiteId());
    }
}
