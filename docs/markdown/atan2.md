# Implementation `atan2(y, x)`

The implementation of the `atan2` function follows the description found in 
[Wikipedia: Atan2](https://en.wikipedia.org/wiki/Atan2)
and the `Math.atan2()` function.

## Formulas

The function calculates differently for the different input ranges

* if x > 0
    * atan(y/x)

* if x < 0 and y > 0
    * atan(y/x) + π

* if x < 0 and y < 0
    * atan(y/x) - π

* if x < 0 and y = 0
    * +π

* if x = 0 and y > 0
    * +π/2

* if x = 0 and y < 0
    * -π/2

* if x = 0 and y = 0
    * throws `ArithmeticException`





## Performance


