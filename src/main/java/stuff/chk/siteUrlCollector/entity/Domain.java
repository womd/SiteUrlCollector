package stuff.chk.siteUrlCollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Data
@Entity
@Table(name = "domains")
public class Domain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DOMAIN")
    @Column(name = "ID", unique=true, nullable=false, insertable = false, updatable=false)
    private Long Id;

    @Column(name = "tld")
    private String tld;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pageurls")
    private Set<PageUrl> pageUrls;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "skipstrings")
    private Set<SkipString>skipStrings;

}
