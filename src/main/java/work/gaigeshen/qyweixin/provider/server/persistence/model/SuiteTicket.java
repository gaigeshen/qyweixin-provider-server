package work.gaigeshen.qyweixin.provider.server.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "qyweixin_suite_ticket")
@Entity
@Data
public class SuiteTicket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String configId;

    private String config;

    private String suiteId;

    private String suiteTicket;

    private Date updateTime;
}
