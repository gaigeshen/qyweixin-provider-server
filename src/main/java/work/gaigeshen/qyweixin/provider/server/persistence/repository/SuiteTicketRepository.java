package work.gaigeshen.qyweixin.provider.server.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import work.gaigeshen.qyweixin.provider.server.persistence.model.SuiteTicket;

@Repository
public interface SuiteTicketRepository extends JpaRepository<SuiteTicket, Long>, JpaSpecificationExecutor<SuiteTicket> {
}
