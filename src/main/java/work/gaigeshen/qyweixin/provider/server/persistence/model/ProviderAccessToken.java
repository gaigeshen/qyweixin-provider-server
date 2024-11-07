package work.gaigeshen.qyweixin.provider.server.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "qyweixin_provider_access_token")
@Entity
@Data
public class ProviderAccessToken {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String configId;

    private String config;

    private String accessToken;

    private Long expiresIn;

    private Long expiresTimestamp;

    private Date updateTime;
}
