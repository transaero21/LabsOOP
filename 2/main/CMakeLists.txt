add_subdirectory(utils)
add_subdirectory(octet)
add_subdirectory(number)
add_subdirectory(dialog)

add_executable(main main.cpp)
target_link_libraries(main dialog)
