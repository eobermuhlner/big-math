#!/usr/bin/bash

./gradlew :regression.v1_0_0:run
./gradlew :regression.v1_1_0:run
./gradlew :regression.v1_2_0:run
./gradlew :regression.v1_2_1:run
./gradlew :regression.v1_3_0:run
./gradlew :regression.v2_0_0:run
./gradlew :regression.v2_0_1:run
./gradlew :regression.v2_1_0:run
./gradlew :regression.v2_2_0:run
./gradlew :regression.v2_2_1:run
./gradlew :regression.v2_3_0:run
./gradlew :regression.v2_3_1:run
./gradlew :regression.v_current:run

./gradlew :regression.analysis:run

cd regression/analysis
csv2chart --property chart=line --format png *.csv
cd ../..