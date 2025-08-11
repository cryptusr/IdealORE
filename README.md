# IdealORE
This is an implementation of Ideal-Leakage ORE with Linear Storage for Large-Domain Range Queries.

This implementation is a research prototype built for micro-benchmarking purposes, and is not intended to be used in production-level code as it has not been carefully analyzed for potential security flaws.

## Prerequisites
Make sure you have the following installed:
> The Java Pairing-Based Cryptography Library ([JPBC](http://http://gas.dia.unisa.it/projects/jpbc))

## Installation
1. Download jpbc_2_0_0.zip (or jpbc_2_0_0.tar.gz depending on your OS).
2. Unzip it and locate the jar directory (jpbc-2.0.0\jars).
3. Add these external jar files in the directory into your Java build path.
4. Locate the curves directory (jpbc-2.0.0\params\curves).
5. Add curve files to use for pairings in your project directory.

## Running a Test
Run Main.java in which you specify the plaintext bit-length (line 24) and the two numbers to be encrypted and then compared (lines 28-29).
