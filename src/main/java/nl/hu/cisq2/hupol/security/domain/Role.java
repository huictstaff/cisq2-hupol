package nl.hu.cisq2.hupol.security.domain;

/**
 * By default, Spring roles start with ROLE_
 * See: <a href="https://docs.spring.io/spring-security/reference/servlet/appendix/faq.html#appendix-faq-role-prefix">docs</a>
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN,
}
