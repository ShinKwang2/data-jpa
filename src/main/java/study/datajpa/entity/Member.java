package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username")
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder(builderMethodName = "withName", buildMethodName = "nameBuild")
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

    public void changeName(String name) {
        this.username = name;
    }
}
