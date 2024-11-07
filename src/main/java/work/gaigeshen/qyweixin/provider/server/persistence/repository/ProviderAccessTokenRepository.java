package work.gaigeshen.qyweixin.provider.server.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import work.gaigeshen.qyweixin.provider.server.persistence.model.ProviderAccessToken;

@Repository
public interface ProviderAccessTokenRepository extends JpaRepository<ProviderAccessToken, Long>, JpaSpecificationExecutor<ProviderAccessToken> {

}
