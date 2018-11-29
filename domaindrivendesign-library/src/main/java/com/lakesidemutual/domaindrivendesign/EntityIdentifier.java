package com.lakesidemutual.domaindrivendesign;

/**
 * An experimental interface to mark @Entity identifiers.
 *
 * @param <T>
 *            The type of the underlying implementation, e.g. a String or UUID.
 */
public interface EntityIdentifier<T> {

	T getId();

}