package work.gaigeshen.qyweixin.provider.server.notify.message.notify;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import work.gaigeshen.qyweixin.provider.server.persistence.repository.PermanentCodeRepository;
import work.gaigeshen.tripartite.core.notify.DefaultNotifyContent;

import javax.persistence.criteria.Predicate;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 企业微信取消授权通知推送处理
 *
 * @author gaigeshen
 */
@RequiredArgsConstructor
@Slf4j
public class QyWeixinCancelAuthMessageNotifyContentProcessor extends QyWeixinMessageNotifyContentProcessor {

    private final PermanentCodeRepository permanentCodeRepository;

    private final Executor executorService;

    @Override
    protected boolean supportsMessageContent(Map<?, ?> messageContent) {
        return StringUtils.equalsIgnoreCase((CharSequence) messageContent.get("InfoType"), "cancel_auth");
    }

    @Override
    protected void processMessageContent(Map<?, ?> messageContent, DefaultNotifyContent content, ProcessorChain<DefaultNotifyContent> chain) {
        String suiteId = (String) messageContent.get("SuiteId");
        String authCorpId = (String) messageContent.get("AuthCorpId");
        if (!StringUtils.isAnyBlank(suiteId, authCorpId)) {
            executorService.execute(() -> {
                try {
                    permanentCodeRepository.findOne((root, query, builder) -> {
                        Predicate predicate1 = builder.equal(root.get("suiteId"), suiteId);
                        Predicate predicate2 = builder.equal(root.get("corpId"), authCorpId);
                        return builder.and(predicate1, predicate2);
                    }).ifPresent(permanentCode -> {
                        permanentCodeRepository.delete(permanentCode);
                        log.info("cancel auth: {}(suite), {}(auth corp name)", permanentCode.getSuiteId(), permanentCode.getCorpName());
                    });
                } catch (Exception e) {
                    log.warn("could not delete permanent code", e);
                }
            });
        }
    }
}
