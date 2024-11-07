package work.gaigeshen.qyweixin.provider.server.notify.message.notify;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import work.gaigeshen.qyweixin.provider.server.client.QyWeixinProviderClient;
import work.gaigeshen.qyweixin.provider.server.client.suite.QyWeixinSuiteTicket;
import work.gaigeshen.tripartite.core.notify.DefaultNotifyContent;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 企业微信应用票据通知推送处理
 *
 * @author gaigeshen
 */
@RequiredArgsConstructor
@Slf4j
public class QyWeixinSuiteTicketMessageNotifyContentProcessor extends QyWeixinMessageNotifyContentProcessor {

    private final QyWeixinProviderClient providerClient;

    private final Executor executorService;

    @Override
    protected boolean supportsMessageContent(Map<?, ?> messageContent) {
        return StringUtils.equalsIgnoreCase((CharSequence) messageContent.get("InfoType"), "suite_ticket");
    }

    @Override
    protected void processMessageContent(Map<?, ?> messageContent, DefaultNotifyContent content, ProcessorChain<DefaultNotifyContent> chain) {
        String suiteId = (String) messageContent.get("SuiteId");
        String suiteTicket = (String) messageContent.get("SuiteTicket");
        if (!StringUtils.isAnyBlank(suiteId, suiteTicket)) {
            executorService.execute(() -> {
                try {
                    QyWeixinSuiteTicket newSuiteTicket = QyWeixinSuiteTicket.builder().setSuiteId(suiteId).setTicket(suiteTicket).build();
                    providerClient.setNewSuiteTicket(newSuiteTicket);
                    log.info("set new suite ticket: {}(ticket), {}(suite)", suiteTicket, suiteId);
                } catch (Exception e) {
                    log.warn("could not set new suite ticket", e);
                }
            });
        }
    }
}
