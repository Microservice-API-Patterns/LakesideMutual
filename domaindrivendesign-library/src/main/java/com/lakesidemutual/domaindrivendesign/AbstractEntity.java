package com.lakesidemutual.domaindrivendesign;

/**
 * An experimental abstract base class for @Entity that could be used to
 * abstract over common features of entities.
 */
public abstract class AbstractEntity<Id, T extends EntityIdentifier<Id>> implements Entity {
	private T localId;

	AbstractEntity(T id) {
		this.localId = id;
	}

	// just a placeholder/demonstrator (could add more methods from "DDD metamodel")
	abstract public String modifyEntity(String parameters);

	public T getLocalId() {
		return localId;
	}
}
