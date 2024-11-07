package work.gaigeshen.qyweixin.provider.server.notify.message.notify;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import work.gaigeshen.qyweixin.provider.server.client.QyWeixinProviderClient;
import work.gaigeshen.qyweixin.provider.server.client.parameters.auth.QyWeixinPermanentCodeGetParameters;
import work.gaigeshen.qyweixin.provider.server.client.response.auth.QyWeixinPermanentCodeGetResponse;
import work.gaigeshen.qyweixin.provider.server.persistence.model.PermanentCode;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.PermanentCodeRepository;
import work.gaigeshen.tripartite.core.notify.DefaultNotifyContent;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * 企业微信授权或者重置授权通知推送处理
 *
 * @author gaigeshen
 */
@RequiredArgsConstructor
@Slf4j
public class QyWeixinCreateAuthMessageNotifyContentProcessor extends QyWeixinMessageNotifyContentProcessor {

    private final QyWeixinProviderClient providerClient;

    private final PermanentCodeRepository permanentCodeRepository;

    private final Executor executorService;

    @Override
    protected boolean supportsMessageContent(Map<?, ?> messageContent) {
        return StringUtils.equalsAnyIgnoreCase((CharSequence) messageContent.get("InfoType"), "create_auth", "reset_permanent_code");
    }

    @Override
    protected void processMessageContent(Map<?, ?> messageContent, DefaultNotifyContent content, ProcessorChain<DefaultNotifyContent> chain) {
        String suiteId = (String) messageContent.get("SuiteId");
        String authCode = (String) messageContent.get("AuthCode");
        if (!StringUtils.isAnyBlank(suiteId, authCode)) {
            executorService.execute(() -> {
                QyWeixinPermanentCodeGetParameters parameters = new QyWeixinPermanentCodeGetParameters();
                parameters.auth_code = authCode;
                QyWeixinPermanentCodeGetResponse response = providerClient.permanentCodeGet(parameters);
                String permanentCode = response.permanent_code;
                Optional<PermanentCode> existsPermanentCodeOptional = permanentCodeRepository.findOne((root, query, builder) -> {
                    Predicate predicate1 = builder.equal(root.get("suiteId"), suiteId);
                    Predicate predicate2 = builder.equal(root.get("corpId"), response.auth_corp_info.corpid);
                    return builder.and(predicate1, predicate2);
                });
                try {
                    PermanentCode updatePermanentCode = existsPermanentCodeOptional.orElseGet(PermanentCode::new);
                    updatePermanentCode.setSuiteId(suiteId);
                    updatePermanentCode.setCorpId(response.auth_corp_info.corpid);
                    updatePermanentCode.setCorpName(response.auth_corp_info.corp_name);
                    updatePermanentCode.setPermanentCode(permanentCode);
                    updatePermanentCode.setAgentId(response.auth_info.agent.iterator().next().agentid);
                    updatePermanentCode.setUpdateTime(new Date());
                    permanentCodeRepository.save(updatePermanentCode);
                    log.info("permanent code saved: {}, {}(auth code)", permanentCode, authCode);
                } catch (Exception e) {
                    log.warn("could not save permanent code", e);
                }
            });
        }
    }
}
