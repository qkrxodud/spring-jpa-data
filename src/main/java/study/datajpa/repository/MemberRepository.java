package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.Entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    @Query("select m from Member m where m.userName = :userName and m.age = :age")
    List<Member> findUser(@Param("userName") String userName, @Param("age") int age);
}

