package com.kenwang.kenapps.api

import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.Suite

@Ignore("Ignore API test")
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ArmRecyclerAPITest::class,
    GarbageTruckAPITest::class,
    ParkingListAPITest::class
)
class APITestSuite
