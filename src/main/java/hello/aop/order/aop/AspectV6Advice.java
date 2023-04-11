package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    /**
     * @Around를 제외한 나머지 어드바이스들은 @Around 어드바이스가 할 수 있는 일의 일부만 제공한다.
     * 즉, @Around 어드바이스만으로도 필요한 기능을 모두 수행할 수 있다.
     *
     * 모든 어드바이스는 JoinPoint를 첫번째 파라미터에 사용할 수도, 생략할 수도 있다.
     * 하지만 @Around 어드바이스는 ProceedingJoinPoint를 사용해야 한다.
     *
     * ProceedingJoinPoint는 org.aspectj.lang.JoinPoint의 하위 타입이다.
     */

    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            //@Before
            //@Around에서는 joinPoint.proceed()를 호출해야 다음 대상이 호출됨
            log.info("[around][트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();

            //@AfterReturning
            //@Around에서는 result 값을 변환할 수 있음
            log.info("[around][트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //AfterThrowing
            log.info("[around][트랜잭션 롤백 {}", joinPoint.getSignature());
            throw e;
        } finally {
            //@After
            log.info("[around][리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        //JoinPoint가 없어도 간단한 로그를 남길 수 있음
        //@Before는 메서드 종료시 자동으로 다음 타겟이 호출됨
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        //@AfterReturning에서는 result를 반환하지 않기 때문에 result값을 변경할 수 없음
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
