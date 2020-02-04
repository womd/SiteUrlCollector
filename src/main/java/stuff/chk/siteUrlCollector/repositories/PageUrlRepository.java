package stuff.chk.siteUrlCollector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.PageUrl;

import java.util.List;

@Repository
public interface PageUrlRepository extends JpaRepository<PageUrl,Long> {
    PageUrl findByUrl(String url);
    List<PageUrl> findByDomain(Domain domain);


    @Query(value = "select * from pageurls pu " +
            "left outer join visits v on v.pageurl = pu.Id " +
            "where pu.domain = :domainId and v is null LIMIT :limit", nativeQuery = true)
    List<PageUrl> findByDomainWithNoVisits (@Param("domainId") Long domainId, @Param("limit") Long limit);

   // List<PageUrl> findFirstByDomainAndVisitsIsNull(Domain domain);

}
