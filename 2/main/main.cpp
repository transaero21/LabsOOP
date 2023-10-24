#include <iostream>
#include <Dialog.h>

int main() {
    try {
        dialog();
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return 1;
    }
    return 0;
}
