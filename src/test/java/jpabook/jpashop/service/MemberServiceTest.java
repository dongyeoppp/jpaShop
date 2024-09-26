package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;

import jpabook.jpashop.repository.MemberRepositoryOld;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)        // 스프링과 테스트 통합
@SpringBootTest     // 스프링 부트 띄우고 테스트 (이게 없으면 @Autowired다 실패)
@Transactional      // 반복가능한 테스트 지원, 테스트가 끝나면 트랜잭션을 강제로 롤백
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member,memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        try{
            memberService.join(member2);
        } catch (IllegalStateException e){
            return;
        }
        //then
        fail("예외가 발생해야 합니다.");
    }
}