#include <iostream>
#include <limits>

#include "Utils.h"

int getInt(int min) {
    int value;

    while (true) {
        std::cin >> value;
        if (std::cin.eof()) {
            throw std::runtime_error("Exiting...");
        } else if (std::cin.bad()) {
            throw std::runtime_error("Failed to read int!");
        } else if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        } else if (value >= min) return value;
        std::cout << "Wrong value, repeat: ";
    }
}