package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //findMember == member

    }

    @Test
    void basicCRUD() {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();

        //카운트 검증
        long count = memberJpaRepository.count();

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();

        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        assertThat(all.size()).isEqualTo(2);

        assertThat(count).isEqualTo(2);

        assertThat(deletedCount).isEqualTo(0);
    }
    
    @Test
    void findByUsernameAndAgeGreaterThan() {
        //given
        Member m1 = Member.builder().username("AAA").age(10).build();
        Member m2 = Member.builder().username("AAA").age(20).build();

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        //when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member m1 = Member.builder().username("AAA").age(10).build();
        Member m2 = Member.builder().username("BBB").age(20).build();
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void paging() {
        //given
        memberJpaRepository.save(Member.builder().username("member1").age(10).build());
        memberJpaRepository.save(Member.builder().username("member2").age(10).build());
        memberJpaRepository.save(Member.builder().username("member3").age(10).build());
        memberJpaRepository.save(Member.builder().username("member4").age(10).build());
        memberJpaRepository.save(Member.builder().username("member5").age(10).build());
        memberJpaRepository.save(Member.builder().username("member6").age(10).build());

        // page1 -> offset=0, limit=10, page2 -> offset=10, limit=10

        int age = 10;
        int offset = 1;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ...
        // 최초 페이지 ...

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(6L);
    }

    @Test
    public void bulkUpdate() {
        //given
        memberJpaRepository.save(Member.builder().username("member1").age(10).build());
        memberJpaRepository.save(Member.builder().username("member2").age(19).build());
        memberJpaRepository.save(Member.builder().username("member3").age(20).build());
        memberJpaRepository.save(Member.builder().username("member4").age(21).build());
        memberJpaRepository.save(Member.builder().username("member5").age(40).build());
        memberJpaRepository.save(Member.builder().username("member6").age(46).build());

        //when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(4);
    }
}