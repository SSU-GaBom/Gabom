package GaBom.Bom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
//@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    @JsonBackReference
    private Travel travel;

//    private String userNickname;

//    private String appendDate;
    //수정 시각각
    private String updateDate;
    //연관관계 편의
    private String Nickname;

//    @Column(columnDefinition = "LONGTEXT")
//    private String profileImage;
}
