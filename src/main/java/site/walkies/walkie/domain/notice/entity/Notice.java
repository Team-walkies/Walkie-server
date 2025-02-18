package site.walkies.walkie.domain.notice.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notice")
@Getter
public class Notice {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "notice_date")
    private LocalDate noticeDate;

    @Column(name = "title", columnDefinition = "MEDIUMTEXT")
    private String title;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;
}