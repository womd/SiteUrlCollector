package stuff.chk.siteUrlCollector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.SkipString;

import java.util.List;

@Repository
public interface SkipStringRepository extends JpaRepository<SkipString, Long> {
    List<SkipString> findByDomain(Domain domain);
}
