package stuff.chk.siteUrlCollector.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import stuff.chk.siteUrlCollector.entity.Domain;
import stuff.chk.siteUrlCollector.entity.SkipString;

import java.util.ArrayList;
import java.util.List;

@Component
public class JSoupCrawler implements ICrawler {

    @Override
    public List<String> extractLinks(String url) throws Exception{
        ArrayList<String> result = new ArrayList();
        Document doc = Jsoup.connect(url).get();// enter the url

        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

// href ...To get all the href on that website
        for (Element link : links) {
            String linkSrc = link.attr("abs:href");
            //distinct only unique links
            if(!result.contains(linkSrc)){
                result.add(linkSrc);
            }

        }

// img ...to get the images from website
        //    for (Element src : media) {
        //        result.add(src.attr("abs:src"));
        //    }

// js, css, ...
        //     for (Element link : imports) {
        //         result.add(link.attr("abs:href"));
        //     }
        return result;
    }

    @Override
    public List<String> filterLinks(List<String>links, List<SkipString>skipList, Domain domain){

        List<String>result = new ArrayList<>();
        for(String link : links){

            boolean skip = false;

            if(link.length() == 0){
                skip = true;
            }

            //only internal links
            if(!link.contains(domain.getTld())){
                skip = true;
            }


            for(SkipString sk : skipList){
                if(link.contains(sk.getValue())){
                    skip = true;
                }
            }


            if(skip == false){
                if(!result.contains(link)) {
                    result.add(link);
                }
            }
        }
        return result;
    }




}
