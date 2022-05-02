package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder(builderMethodName = "name", buildMethodName = "of")
    public Member(String username) {
        this.username = username;
    }

    @Builder
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public static Member createMemberWithTeam(String username, int age, Team team) {
        Member member = Member.builder()
                .username(username)
                .age(age)
                .build();

        if (team != null) {
            member.changeTeam(team);
        }
        return member;
    }


    //연관관계 Setting 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
