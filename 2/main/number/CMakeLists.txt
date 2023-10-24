add_library(number STATIC Number.cpp)
target_include_directories(number PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
target_link_libraries(number octet)
