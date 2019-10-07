# Performance Regression

The same performance measurements for the interesting mathematical functions
in `BigDecimalMath` can be executed with different releases of `big-math`.  

This allows to compare the performance regression over all releases.

## Run with gradle

To run the performance measurements execute the following command (change version number as desired):
```console
./gradlew :regression.v1_0_0:run
```

The special version string `v_current` may be used to measure the performance of the current checkout.
```console
./gradlew :regression.v_current:run
```


## Measured performance

The committed `performance.csv` files in each regression project where created
by running on a PC with the following specs: 

```
Caption                               DeviceID  MaxClockSpeed  Name                                      NumberOfCores  Status
Intel64 Family 6 Model 78 Stepping 3  CPU0      2592           Intel(R) Core(TM) i7-6500U CPU @ 2.50GHz  2              OK
```

Note: the CPU specs where printed with the following command: 

```console
wmic cpu get caption, deviceid, name, numberofcores, maxclockspeed, status
```
