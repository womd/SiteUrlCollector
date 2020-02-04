package stuff.chk.siteUrlCollector.crawler;

import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.SkipString;

import java.util.List;

public interface ICrawler {

    List<String> extractLinks(String url) throws Exception;

    List<String> filterLinks(List<String>links, List<SkipString>skipStrings, Domain domain);
}
