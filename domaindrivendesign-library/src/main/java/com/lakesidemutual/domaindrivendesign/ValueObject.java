package com.lakesidemutual.domaindrivendesign;

/**
 * A marker interfaces for Value Objects. See <a href=
 * "https://domainlanguage.com/ddd/reference/">https://domainlanguage.com/ddd/reference/</a>
 * for details.
 * 
 * Value Objects are immutable and have structural equality, so they need to
 * implement equals and hashCode. Unfortunately, this can't be enforced in the
 * Java type system, so this remains a marker interface without any methods.
 */
public interface ValueObject {

}
