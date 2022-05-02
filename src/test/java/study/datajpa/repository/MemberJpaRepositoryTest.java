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
}