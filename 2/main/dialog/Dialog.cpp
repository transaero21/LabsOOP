#include <Utils.h>

#include "Dialog.h"

void dialog() {
    Number num = Number(0);

    while (true) {
        std::cout << "Your number: " << num << '\n';
        for (size_t i = 0; i < count; i++) {
            std::cout << i + 1 << ". " << options[i].text << '\n';
        }
        std::cout << "Your choice: ";

        int option = getNumber<int>(1, (int) count) - 1;
        if (!options[option].fun) break;
        options[option].fun(num);
        std::cout << '\n';
    }
}

static Number getNumber() {
    Number a = Number(0);
    while(true) {
        std::cin >> a;
        if (std::cin.eof()) {
            throw std::runtime_error("Exiting...");
        } else if (std::cin.bad()) {
            throw std::runtime_error("Failed to read number!");
        } else if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Wrong value, repeat: ";
        }
        return a;
    }
}

void setNumber(Number &num) {
    std::cout << "Input number: ";
    num = getNumber();
}

void addNumbers(Number &num) {
    std::cout << "Input another number: ";
    Number result = num + getNumber();
    std::cout << "Result: " << result << "\nKeep result or not: ";
    if (getNumber<int>())
        num = result;
}

void getNegative(Number &num) {
    std::cout << -num << std::endl;
}

void incrementNumber(Number &num) {
    num++;
}

void decrementNumber(Number &num) {
    num--;
}
