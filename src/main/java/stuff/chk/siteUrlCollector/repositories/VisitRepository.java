package stuff.chk.siteUrlCollector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stuff.chk.siteUrlCollector.entity.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
}
