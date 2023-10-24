add_library(dialog STATIC Dialog.cpp)
target_include_directories(number PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
target_link_libraries(dialog number utils)
