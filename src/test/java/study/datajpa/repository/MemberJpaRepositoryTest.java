package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository jpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = jpaRepository.save(member);
        Member findMember = jpaRepository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        jpaRepository.save(member1);
        jpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = jpaRepository.findById(member1.getId()).get();
        Member findMember2 = jpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = jpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = jpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        jpaRepository.delete(member1);
        jpaRepository.delete(member2);
        long deletedCount = jpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUserNameAndGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        jpaRepository.save(m1);
        jpaRepository.save(m2);

        List<Member> result = jpaRepository.findByUserNameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUserName()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.get(0)).isEqualTo(m2);
    }

    @Test
    public void 페이징확인() throws Exception {

        //given
        jpaRepository.save(new Member("member1", 10));
        jpaRepository.save(new Member("member2", 10));
        jpaRepository.save(new Member("member3", 10));
        jpaRepository.save(new Member("member4", 10));
        jpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = jpaRepository.findByPage(age, offset, limit);
        long totalCount = jpaRepository.totalCount(age);

        //then
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    public void 벌크업데이트테스트() {
        //given
        jpaRepository.save(new Member("member1", 10));
        jpaRepository.save(new Member("member1", 19));
        jpaRepository.save(new Member("member1", 20));
        jpaRepository.save(new Member("member1", 21));
        jpaRepository.save(new Member("member1", 40));

        //when
        int resultCount = jpaRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);

    }
}