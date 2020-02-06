package stuff.chk.siteUrlCollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "skipstrings")
@SequenceGenerator(name = "SQ_SKIPSTRING", sequenceName = "SQ_SKIPSTRING",  initialValue = 100)
public class SkipString implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SKIPSTRING")
    private Long Id;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domains")
    private Domain domain;

}
