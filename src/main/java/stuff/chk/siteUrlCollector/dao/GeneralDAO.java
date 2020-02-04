package stuff.chk.siteUrlCollector.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.PageUrl;
import stuff.chk.siteUrlCollector.entity.SkipString;
import stuff.chk.siteUrlCollector.entity.Visit;
import stuff.chk.siteUrlCollector.repositories.DomainRepository;
import stuff.chk.siteUrlCollector.repositories.PageUrlRepository;
import stuff.chk.siteUrlCollector.repositories.SkipStringRepository;
import stuff.chk.siteUrlCollector.repositories.VisitRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Data
public class GeneralDAO {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private PageUrlRepository pageUrlRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private SkipStringRepository skipStringRepository;

    public boolean saveLinks(List<String>links, Domain domain){

        try {
            for (String link : links) {


                //only if not exist
                if(pageUrlRepository.findByUrl(link) == null) {

                    PageUrl pu = new PageUrl();
                    pu.setUrl(link);
                    pu.setDomain(domain);
                    pageUrlRepository.save(pu);
                }
            }
            return true;
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            return false;
        }

    }

    public List<String>filterExisting(List<String>links) {
        List<String>result = new ArrayList<>();
        for(String link : links){
            if (pageUrlRepository.findByUrl(link) == null) {
                result.add(link);
            }
        }
        return  result;
    }


    public boolean saveVisit(PageUrl pu, int responseStatus){

        try {
            Visit v = new Visit();
            v.setProcessedAt(Date.valueOf(LocalDate.now()));
            v.setUrl(pu);
            v.setResponseStatus(responseStatus);
            visitRepository.save(v);
            return true;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean saveSkipString(Domain domain, String value){
        try{
            SkipString sk = new SkipString();
            sk.setDomain(domain);
            sk.setValue(value);
            skipStringRepository.save(sk);
            return true;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public Domain addDomain(String value ){
        try {
            Domain domain = new Domain();
            domain.setTld(value);
            domain = domainRepository.save(domain);
            return domain;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

}
