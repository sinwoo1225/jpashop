package siru.jpashop.repository;

import org.springframework.stereotype.Repository;
import siru.jpashop.domain.Address;
import siru.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
