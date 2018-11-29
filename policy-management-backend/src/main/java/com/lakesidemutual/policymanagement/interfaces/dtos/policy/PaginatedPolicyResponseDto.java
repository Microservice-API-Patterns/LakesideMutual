package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * The PaginatedPolicyResponseDto holds a collection of PolicyDto
 * with additional metadata parameters such as the limit, offset and size that
 * are used in the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/WADE-Pagination.html">Pagination</a>
 * pattern, specifically the <em>Offset-Based</em> Pagination variant.
 */
public class PaginatedPolicyResponseDto extends ResourceSupport {
	private final int limit;
	private final int offset;
	private final int size;
	private final List<PolicyDto> policies;

	public PaginatedPolicyResponseDto(int limit, int offset, int size, List<PolicyDto> policies) {
		this.limit = limit;
		this.offset = offset;
		this.size = size;
		this.policies = policies;
	}

	public List<PolicyDto> getPolicies() {
		return policies;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}
}