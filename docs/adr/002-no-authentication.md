# ADR 002: No Authentication

## Context

This project is a domain modeling showcase, not a production system. Adding OAuth2/JWT infrastructure would increase setup complexity and distract from domain design discussions.

## Decision

All endpoints are public. SecurityConfig permits all requests. No authentication or role-based access control.

## Consequences

- ✓ Zero setup for reviewers - clone, run, explore
- ✓ Focus remains on domain design and banking logic
- ✗ Not representative of production banking security
  - Production deployments would integrate Spring Security OAuth2 resource server with external identity provider
  - All endpoints would require valid JWT token with appropriate scopes
