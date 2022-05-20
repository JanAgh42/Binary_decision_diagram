The goal of this exercise was to create our own implementation of BDD (Binary decision diagram).

My implementation accepts and evaluates all kinds of Boolean functions, provided that they are in DNF form. It also utilizes an extensive two-part reduction with hash maps during the diagram's build process that greatly improves the effectivity of my implementation and reduces the execution time.

The project also contains a basic testing program (inside Main.java) that can be used to evaluate Boolean functions in various different ways and also measure the effectivity of the diagram itself.