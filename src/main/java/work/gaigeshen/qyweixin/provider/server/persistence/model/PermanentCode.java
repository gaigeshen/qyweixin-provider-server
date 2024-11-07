package work.gaigeshen.qyweixin.provider.server.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "qyweixin_permanent_code")
@Entity
@Data
public class PermanentCode {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String suiteId;

    private String corpId;

    private String corpName;

    private String permanentCode;

    private Integer agentId;

    private Date updateTime;
}
