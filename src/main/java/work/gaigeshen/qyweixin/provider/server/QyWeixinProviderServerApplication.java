package work.gaigeshen.qyweixin.provider.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author gaigeshen
 */
@SpringBootApplication
@Slf4j
public class QyWeixinProviderServerApplication {

    public static void main(String[] args) {
        new SpringApplication(QyWeixinProviderServerApplication.class).run(args);
    }
}
