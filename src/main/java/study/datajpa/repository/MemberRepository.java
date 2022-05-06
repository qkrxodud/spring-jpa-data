package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.Entity.Member;
import study.datajpa.dto.MemberDto;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    @Query("select m from Member m where m.userName = :userName and m.age = :age")
    List<Member> findUser(@Param("userName") String userName, @Param("age") int age);

    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //컬렌션 조회
    List<Member> findMemberListByUserName(String name);

    //단건 조회
    Member findMemberByUserName(String name);

    //단건 Optional 조회
    Optional<Member> findOptionalMemberByUserName(String name);

    /**
     * Page 사용 예제 정의코드
     */
    Page<Member> findByAge(int age, Pageable pageable);

    //count 쿼리 사용
    Page<Member> findMemberPageByUserName(String name, Pageable pageable);

    //count 쿼리 사용안함
    Slice<Member> findMemberSliceByUserName(String name, Pageable pageable);

    //count 쿼리 사용 안함
    List<Member> findMemberListByUserName(String name, Pageable pageable);

    List<Member> findByUserName(String name, Sort sort);

    @Query(value = "select m from Member m", countQuery = "select count(m.userName) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    List<Member> findTop2By();
}


