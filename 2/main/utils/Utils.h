#ifndef UTILS_H
#define UTILS_H

#include <iostream>
#include <limits>

template<class T>
T getNumber(T min = std::numeric_limits<T>::lowest(), T max = std::numeric_limits<T>::max()) {
    T value;
    while(true) {
        std::cin >> value;
        if (std::cin.eof()) {
            throw std::runtime_error("Exiting...");
        } else if (std::cin.bad()) {
            throw std::runtime_error("Failed to read number!");
        } else if (std::cin.fail() || value < min || value > max) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Wrong value, repeat: ";
        } else return value;
    }
}

#endif /* UTILS_H */
