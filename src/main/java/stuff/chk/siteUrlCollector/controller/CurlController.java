package stuff.chk.siteUrlCollector.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stuff.chk.siteUrlCollector.crawler.JSoupCrawler;
import stuff.chk.siteUrlCollector.dao.GeneralDAO;
import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.PageUrl;
import stuff.chk.siteUrlCollector.entity.SkipString;

import java.util.List;

@Slf4j
@Data
@RestController
@RequestMapping("${base.uri}")
public class CurlController {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private JSoupCrawler crawler;

    @RequestMapping(value = "addSkipString", method = RequestMethod.GET)
    public ResponseEntity addSkipString(@RequestParam(value = "domainId") Long domainId, @RequestParam(value="skipString") String skipString){

        try {
            Domain domain = generalDAO.getDomainRepository().findById(domainId).orElse(null);
            if (domain != null) {
                generalDAO.saveSkipString(domain,skipString);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());

        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "listSkipStrings", method = RequestMethod.GET)
    public ResponseEntity listSkipStrings(@RequestParam(value = "domainId") Long domainId){
        try {
            Domain domain = generalDAO.getDomainRepository().findById(domainId).orElse(null);
            List<SkipString>list = generalDAO.getSkipStringRepository().findByDomain(domain);
            return new ResponseEntity<>(list, HttpStatus.OK);

        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "deleteSkipString", method = RequestMethod.GET)
    public ResponseEntity deleteSkipString(@RequestParam(value = "skipStringId") Long skipStringId){
        try {
            SkipString sk = generalDAO.getSkipStringRepository().findById(skipStringId).orElse(null);
            generalDAO.getSkipStringRepository().delete(sk);
            return new ResponseEntity<>(true, HttpStatus.OK);

        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "addDomain", method = RequestMethod.GET)
    public ResponseEntity addDomain(@RequestParam(value="tld") String tld){

        try {
            Domain domain = generalDAO.getDomainRepository().findByTld(tld);
            if (domain != null) {
                return new ResponseEntity<>(domain, HttpStatus.OK);
            } else {
                Domain saved = generalDAO.addDomain(tld);
                return new ResponseEntity<>(saved, HttpStatus.OK);
            }
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "startForDomain", method = RequestMethod.GET)
    public ResponseEntity startForDomain(@RequestParam(value = "domainId") Long domainId){

        try {

            Domain domain = generalDAO.getDomainRepository().findById(domainId).orElse(null);
            if(domain != null) {
                List<String> links = crawler.extractLinks(domain.getTld());
                List<SkipString> skipStrings =  generalDAO.getSkipStringRepository().findByDomain(domain);
                List<String> filteredLinks = crawler.filterLinks(links, skipStrings ,domain);
                List<String> onlyNew = generalDAO.filterExisting(filteredLinks);
                generalDAO.saveLinks(onlyNew, domain);
            }
            return new ResponseEntity<>("ok, process started", HttpStatus.OK);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "listUrlsForDomain", method = RequestMethod.GET)
    public ResponseEntity listUrlsForDomain(@RequestParam(value = "domainId") Long domainId){
        try {
            Domain domain = generalDAO.getDomainRepository().findById(domainId).orElse(null);
            List<PageUrl>list = generalDAO.getPageUrlRepository().findByDomain(domain);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
