# Cryptographic library and app

A SHA-3/SHAKE cryptographic library and a command line app to test the library. It can utilize all the cryptographic algorithms that fall under the category of symmetric. Uses specifications found in https://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.202.pdf.

## Understanding the algorithm (contribute to this section how you see fit)

### What is KECCAK?

KECCAK is the cryptographic hash function that was selected as the winner of the NIST competition to become the SHA-3 (Secure Hash Algorithm 3) standard. While KECCAK and SHA-3 are often used interchangeably, there are a few subtle differences, mainly in how padding is applied, but the core algorithm is the same.

KECCAK uses a **sponge construction**, a framework that absorbs an input and then squeezes out the hash. It involves two phases:

1. **Absorbing:** The input message is processed in chunks, XORed with an internal state.
2. **Squeezing:** Once the message is fully absorbed, the hash is "squeezed out" from the internal state to produce the final output.

### What are five step mapping functions θ, ρ, π, χ, ι, and what phases do they fit in?

1. Theta (θ) - Diffusion Step
    * **Functionality**: This function provides mixing between all bits in the state, creating diffusion across the rows and columns. It applies a parity check across columns of the 5x5 matrix of slices in the state.

    * **Effect**: Theta ensures that each bit is affected by the bits of every column, propagating local changes across the entire state.

2. Rho (ρ) - Bitwise Rotation
    * **Functionality**: It rotates the bits of each lane (individual segments of the state matrix) by a position-dependent number of steps.

    * **Effect**: This step provides non-linearity by rotating bits in different ways for each lane, contributing to the complexity of the hash.

3. Pi (π) - Transposition (Permutation)
    * **Functionality**: This step rearranges the positions of the bits within the 3D state array.

    * **Effect**: Pi ensures that bits are mixed across different lanes.

4. Chi (χ) - Nonlinear Mixing

    * **Functionality**: It introduces non-linearity into the state by XORing each bit with a combination of other bits in the same row.

    * **Effect**: Chi introduces non-linearity, which is critical for creating a secure cryptographic transformation that resists linear attacks.

5. Iota (ι) - Round Constant Addition

    * **Functionality**: This step injects a round-dependent constant into the state to break symmetry and ensure that each round is different.

    * **Effect**: Iota ensures that the permutations applied in each round differ, preventing any symmetry or structure from weakening the hash function.

### Why do we use XOR?

* **Reversible Operation:** XOR is easily reversible, which means you can use XOR to encode and then decode data. If you XOR the result again with the same data, you get back the original data.

* **Mixing Data (Diffusion):** XOR is commonly used to combine different pieces of data in a way that "mixes" them. This mixing helps distribute changes across the data, which is important for confusion and diffusion, two principles in cryptography that aim to obscure relationships between the input and output data.

* **Efficient Bit Manipulation:** XOR can quickly combine data bit-by-bit, making it ideal for use in algorithms that need to process large amounts of data in small chunks, like KECCAK.

## Contributing

Nathan Hinthorne, Trae Claar, ...