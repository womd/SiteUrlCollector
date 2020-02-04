package stuff.chk.siteUrlCollector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stuff.chk.siteUrlCollector.entity.Domain;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    Domain findByTld(String tld);
}
