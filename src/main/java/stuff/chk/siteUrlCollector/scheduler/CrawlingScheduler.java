package stuff.chk.siteUrlCollector.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import stuff.chk.siteUrlCollector.crawler.JSoupCrawler;
import stuff.chk.siteUrlCollector.dao.GeneralDAO;
import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.PageUrl;
import stuff.chk.siteUrlCollector.entity.SkipString;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Configuration
@EnableScheduling
public class CrawlingScheduler {

    @Autowired
    GeneralDAO generalDAO;

    @Autowired
    JSoupCrawler crawler;

    @Value("${scheduler.crawling.active}")
    private boolean schedulerActive;

    @Scheduled(fixedDelay=10000)
    public void crawl(){

        if(schedulerActive) {

            List<Domain> domains = generalDAO.getDomainRepository().findAll();
            int randomNum = ThreadLocalRandom.current().nextInt(0, domains.size());

            Domain domain = generalDAO.getDomainRepository().findById(domains.get(randomNum).getId()).orElse(null);
            if (domain != null) {
                //first use unvisited
                PageUrl pu = null;
                List<PageUrl> toVisit = generalDAO.getPageUrlRepository().findByDomainWithNoVisits(domain.getId(), 1L);
                if (toVisit.size() > 0) {
                    pu = toVisit.get(0);

                    try {
                        List<String> links = crawler.extractLinks(pu.getUrl());
                        List<SkipString> skipStrings = generalDAO.getSkipStringRepository().findByDomain(domain);
                        List<String> filteredLinks = crawler.filterLinks(links, skipStrings, domain);
                        List<String> onlyNew = generalDAO.filterExisting(filteredLinks);
                        generalDAO.saveLinks(onlyNew, domain);

                        //set visits
                        generalDAO.saveVisit(pu, 200);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        generalDAO.saveVisit(pu, 400);
                    }

                }

            }
        }
    }

}
