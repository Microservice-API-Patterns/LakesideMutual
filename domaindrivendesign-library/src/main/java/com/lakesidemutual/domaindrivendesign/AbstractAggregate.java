package com.lakesidemutual.domaindrivendesign;

/**
 * An experimental abstract base class for @Aggregates that could be used to
 * abstract over common features of aggregates.
 */
public abstract class AbstractAggregate implements Aggregate {

	// just a placeholder/demonstrator:
	abstract public boolean checkInvariants();

	// just a placeholder/demonstrator:
	abstract <T> Entity getRootById(EntityIdentifier<T> globalId);
}
