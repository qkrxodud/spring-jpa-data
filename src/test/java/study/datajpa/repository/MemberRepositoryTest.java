package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Entity.Member;
import study.datajpa.Entity.Team;
import study.datajpa.dto.MemberDto;

import java.awt.print.Pageable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    //메소드 쿼리문 테스트
    @Test
    public void 메소드쿼리테스트() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> user = memberRepository.findUser(member1.getUserName(), member1.getAge());
        assertThat(user.get(0)).isEqualTo(member1);
    }

    //Query DTO 조회
    @Test
    public void 쿼리DTO바로조회() {
        Team team = new Team("AAAA");
        teamRepository.save(team);
        Member member1 = new Member("member1", 10, team);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println(dto.getId());
            System.out.println(dto.getUserName());
            System.out.println(dto.getTeamName());
        }

    }

    //컬렉션 파라미터 바인딩
    @Test
    public void 컬렉션파라미터바인딩() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> names = new ArrayList<>();
        names.add("AAA");
        names.add("BBB");
        names.add("CCC");

        List<Member> byNames = memberRepository.findByNames(names);
        for (Member byName : byNames) {
            System.out.println(byName.getUserName());
        }
    }


    @Test
    public void 반환타입테스트() {
        Member member1 = new Member("AAA", 10);
        memberRepository.save(member1);

        //컬렌션 조회
        List<Member> members = memberRepository.findMemberListByUserName("AAA");
        for (Member member : members) {
            assertThat(member).isEqualTo(member1);
        }
        //단건 조회
        Member member = memberRepository.findMemberByUserName("AAA");
        assertThat(member).isEqualTo(member1);
        //단건 Optional 조회
        Optional<Member> Member = memberRepository.findOptionalMemberByUserName("AAA");
        assertThat(Member.get()).isEqualTo(member1);

    }

    @Test
    public void 페이징확인() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        //when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        page.map(m -> new MemberDto(m.getId(), m.getUserName(), null));

        List<Member> top2By = memberRepository.findTop2By();

        //then
        for (Member member : top2By) {
            System.out.println("top2 >>>> " + member.getUserName());
        }
        List<Member> content = page.getContent(); // 조회된 데이터
        assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가?
    }

    @Test
    public void 벌크업데이트테스트() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member1", 19));
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member1", 21));
        memberRepository.save(new Member("member1", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

}