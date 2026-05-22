# Good and Bad Tests

## Good Tests

**Integration-style**: Test through real interfaces, not mocks of internal parts.

```kotlin
// GOOD: Tests observable behavior
@Test
fun `user can checkout with valid cart`() {
    val cart = createCart()
    cart.add(product)
    
    val result = checkout(cart, paymentMethod)
    
    assertThat(result.status).isEqualTo("confirmed")
}
```

Characteristics:

- Tests behavior users/callers care about
- Uses public API only
- Survives internal refactors
- Describes WHAT, not HOW
- One logical assertion per test

## Bad Tests

**Implementation-detail tests**: Coupled to internal structure.

```kotlin
// BAD: Tests implementation details
@Test
fun `checkout calls paymentService process`() {
    val mockPayment = mockk<PaymentService>()
    every { mockPayment.process(any()) } returns true
    
    checkout(cart, mockPayment)
    
    verify { mockPayment.process(cart.total) }
}
```

Red flags:

- Mocking internal collaborators
- Testing private methods
- Asserting on call counts/order
- Test breaks when refactoring without behavior change
- Test name describes HOW not WHAT
- Verifying through external means instead of interface

```kotlin
// BAD: Bypasses interface to verify
@Test
fun `createUser saves to database`() {
    createUser(User(name = "Alice"))
    
    val row = db.query("SELECT * FROM users WHERE name = 'Alice'")
    
    assertThat(row).isNotNull()
}

// GOOD: Verifies through interface
@Test
fun `createUser makes user retrievable`() {
    val user = createUser(User(name = "Alice"))
    
    val retrieved = getUser(user.id)
    
    assertThat(retrieved.name).isEqualTo("Alice")
}
```
