# Interface Design for Testability

Good interfaces make testing natural in Kotlin:

1. **Accept dependencies, don't create them**

   Use constructor injection for long-lived dependencies and parameters for ephemeral ones.

   ```kotlin
   // Testable: Dependency is injected via constructor
   class OrderService(private val paymentGateway: PaymentGateway) {
       fun processOrder(order: Order) {
           paymentGateway.charge(order.total)
       }
   }

   // Hard to test: Creates its own dependency
   class OrderService {
       fun processOrder(order: Order) {
           val gateway = StripeGateway() // Hidden dependency
           gateway.charge(order.total)
       }
   }
   ```

2. **Return results, don't produce side effects**

   Prefer pure functions that return data representing the state change rather than mutating an object in place.

   ```kotlin
   // Testable: Returns a result (immutability)
   fun calculateDiscount(cart: Cart): Discount {
       return if (cart.total > 100) Discount(10) else Discount(0)
   }

   // Hard to test: Modifies state in-place (side effect)
   fun applyDiscount(cart: Cart) {
       if (cart.total > 100) {
           cart.total -= 10 // Mutates shared state
       }
   }
   ```

3. **Small surface area**
    - **Interface Segregation**: Fewer methods in an interface mean fewer things to mock or stub.
    - **Single Responsibility**: A function should do one thing and return a result.
    - **Parameter objects**: If a function takes many parameters, group them into a `data class`.

4. **Favor Sealed Classes/Interfaces for state**
    - Instead of returning `null` or throwing exceptions, return a `Sealed Class` to represent all possible outcomes (Success, Failure, Loading). This makes it easy for tests to assert on exhaustive states.
