package stuff.chk.siteUrlCollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "pageurls")
@SequenceGenerator(name = "SQ_PAGEURL", sequenceName = "SQ_PAGEURL",  initialValue = 100)
public class PageUrl implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PAGEURL")
    private Long Id;

    @Column(name="PAGEURL", unique = true, nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "DOMAIN", nullable = false)
    private Domain domain;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "visits")
    private Set<Visit> visits;

}
