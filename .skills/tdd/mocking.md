# When to Mock

Mock at **system boundaries** only:

- External APIs (payment, email, etc.)
- Databases (sometimes - prefer test DB)
- Time/randomness
- File system (sometimes)

Don't mock:

- Your own classes/modules
- Internal collaborators
- Anything you control

## Designing for Mockability

At system boundaries, design interfaces that are easy to mock:

**1. Use dependency injection**

Pass external dependencies in rather than creating them internally. This allows you to provide a `mockk()` instance during testing.

```kotlin
// Data objects should use properties (val/var)
data class Order(val total: Int)

// Easy to mock: Dependency is passed in (Constructor or Parameter)
fun processPayment(order: Order, paymentClient: PaymentClient): Result {
    return paymentClient.charge(order.total) // Using property access
}

// Hard to mock: Dependency is created internally
fun processPayment(order: Order): Result {
    val client = StripeClient(System.getenv("STRIPE_KEY"))
    return client.charge(order.total)
}
```

**Testing the injectable version with MockK:**

```kotlin
@Test
fun `processPayment calls charge with order total`() {
    // 1. Arrange: Manual instantiation, no annotations
    val paymentClient = mockk<PaymentClient>()
    val order = Order(total = 100)
    
    // Explicitly define behavior
    every { paymentClient.charge(100) } returns Result.Success

    // 2. Act
    processPayment(order, paymentClient)

    // 3. Assert: Verify the interaction
    verify { paymentClient.charge(100) }
}
```

**Note on Coroutines:** If your boundary uses `suspend` functions, use `coEvery` and `coVerify`.

```kotlin
coEvery { paymentClient.charge(any()) } returns Result.Success
// ...
coVerify { paymentClient.charge(100) }
```

**2. Prefer SDK-style interfaces over generic fetchers**

Create specific functions for each external operation instead of one generic function with conditional logic.

```kotlin
// GOOD: Each function is independently mockable
interface UserApi {
    fun getUser(id: String): User
    fun getOrders(userId: String): List<Order>
    fun createOrder(data: OrderData): Order
}

// BAD: Mocking requires conditional logic inside the mock to handle different endpoints
interface GenericApi {
    fun fetch(endpoint: String, options: Map<String, Any>): Response
}
```

The SDK approach means:
- Each mock returns one specific shape
- No complex `match { ... }` logic in test setup
- Easier to see which endpoints a test exercises
- Type safety per endpoint
