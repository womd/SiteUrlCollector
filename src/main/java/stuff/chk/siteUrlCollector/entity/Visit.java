package stuff.chk.siteUrlCollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table(name = "visits")
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_VISIT")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "PAGEURL")
    private PageUrl url;


    private Integer ResponseStatus;
    private Date processedAt;
}
