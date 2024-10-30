[![Build Status](https://github.com/mP1/j2cl-big-math/workflows/build.yaml/badge.svg)](https://github.com/mP1/j2cl-big-math/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/big-math/badge.svg?branch=master)](https://coveralls.io/github/mP1/big-math?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/big-math.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/big-math/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/big-math.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/big-math/alerts/)
![](https://tokei.rs/b1/github/mP1/j2cl-big-math)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

A fork with minimal changes to the original, only a single class and no other methods was removed or marked `@Gwt-Incompatible`. 

- `BigDecimalMathTest` has been adapted into a j2cl unit test. Long running & multi-threaded tests have been simplified or removed.
- `DefaultBigDecimalMath` has been removed due to ThreadContext usage.
- The core project has been kept, other support modules have been removed.
- build.gradle files replaced with maven POM.

For complete details about features and more refer to the original project [big-math](https://github.com/eobermuhlner/big-math).



## Licensing

Original project [MIT license](license/Eric Obermühlner-big-math-LICENSE.txt).