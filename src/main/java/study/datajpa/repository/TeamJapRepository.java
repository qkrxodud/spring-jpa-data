package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.Entity.Member;
import study.datajpa.Entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TeamJapRepository {

    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    public long Count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }

}
