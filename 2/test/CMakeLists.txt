find_package(GTest REQUIRED)

enable_testing()

add_executable(test OctetTest.cpp NumberTest.cpp)
target_link_libraries(test octet number GTest::gtest GTest::gtest_main)
