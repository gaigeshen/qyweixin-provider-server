package work.gaigeshen.qyweixin.provider.server.notify;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import work.gaigeshen.qyweixin.provider.server.client.QyWeixinProviderClient;
import work.gaigeshen.qyweixin.provider.server.client.config.QyWeixinConfig;
import work.gaigeshen.qyweixin.provider.server.notify.message.ReceiveMessage;
import work.gaigeshen.qyweixin.provider.server.notify.util.SecureUtils;
import work.gaigeshen.qyweixin.provider.server.notify.util.SignatureUtils;
import work.gaigeshen.tripartite.core.notify.AbstractNotifyContentReceiver;
import work.gaigeshen.tripartite.core.notify.DefaultNotifyContent;
import work.gaigeshen.tripartite.core.notify.NotifyContentIncorrectException;
import work.gaigeshen.tripartite.core.util.xml.XmlCodec;

import java.security.GeneralSecurityException;
import java.util.Objects;

/**
 * 企业微信回调通知数据接收器，首先会对接收到的回调通知数据校验签名是否合法，然后进行解密操作，将解密后的内容原封不动加入到原始的异步通知数据里面
 *
 * @author gaigeshen
 */
@RequiredArgsConstructor
public class QyWeixinNotifyContentReceiver extends AbstractNotifyContentReceiver<DefaultNotifyContent> {

    private final QyWeixinProviderClient providerClient;

    @Override
    protected DefaultNotifyContent validate(DefaultNotifyContent content) throws NotifyContentIncorrectException {
        QyWeixinConfig config = providerClient.getConfig();
        String signature = (String) content.getValue("msg_signature");
        if (StringUtils.isBlank(signature)) {
            throw new NotifyContentIncorrectException("could not find [msg_signature] parameter: " + content);
        }
        String timestamp = (String) content.getValue("timestamp");
        if (StringUtils.isBlank(timestamp)) {
            throw new NotifyContentIncorrectException("could not find [timestamp] parameter: " + content);
        }
        String nonce = (String) content.getValue("nonce");
        if (StringUtils.isBlank(nonce)) {
            throw new NotifyContentIncorrectException("could not find [nonce] parameter: " + content);
        }
        String echostr = (String) content.getValue("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            if (!Objects.equals(SignatureUtils.genSignature(config.getToken(), timestamp, nonce, echostr), signature)) {
                throw new NotifyContentIncorrectException("invalid signature: " + content);
            }
            try {
                String decrypt = SecureUtils.decrypt(config.getAesKey(), echostr);
                content.put("echostr", decrypt);
            } catch (GeneralSecurityException e) {
                throw new NotifyContentIncorrectException("could not decrypt: " + content, e);
            }
            return content;
        }
        String bodyString = content.getBodyAsString();
        if (StringUtils.isBlank(bodyString)) {
            throw new NotifyContentIncorrectException("could not find request body: " + content);
        }
        ReceiveMessage receiveMessage = XmlCodec.instance().decodeObject(bodyString, ReceiveMessage.class);
        String encrypted = receiveMessage.getEncrypt();
        if (StringUtils.isBlank(encrypted)) {
            throw new NotifyContentIncorrectException("request body field [Encrypt] is blank: " + content);
        }
        if (!Objects.equals(SignatureUtils.genSignature(config.getToken(), timestamp, nonce, encrypted), signature)) {
            throw new NotifyContentIncorrectException("invalid signature: " + content);
        }
        try {
            content.put("decrypted", SecureUtils.decrypt(config.getAesKey(), encrypted));
        } catch (GeneralSecurityException e) {
            throw new NotifyContentIncorrectException("could not decrypt: " + content, e);
        }
        return content;
    }
}
