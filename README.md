# Cryptographic library and app

A SHA-3/SHAKE cryptographic library and a command lineapp to test the library. Uses specifications found in https://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.202.pdf.

## Understanding the algorithm (contribute to this section how you see fit)

### Why do we use XOR?

* **Reversible Operation:** XOR is easily reversible, which means you can use XOR to encode and then decode data. If you XOR the result again with the same data, you get back the original data.

* **Mixing Data (Diffusion):** XOR is commonly used to combine different pieces of data in a way that "mixes" them. This mixing helps distribute changes across the data, which is important for confusion and diffusion, two principles in cryptography that aim to obscure relationships between the input and output data.

* **Efficient Bit Manipulation:** XOR can quickly combine data bit-by-bit, making it ideal for use in algorithms that need to process large amounts of data in small chunks, like KECCAK.

## Contributing

Nathan Hinthorne, Trae Claar, ...