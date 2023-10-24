#include <iostream>

#include "Matrix.h"

int main() {
    Matrix matrix, newMatrix;

    try {
        matrix = inputMatrix();
        newMatrix = sortMatrix(matrix);
    } catch(const std::exception &e) {
        std::cerr << e.what() << std::endl;
        deleteMatrix(matrix);
        deleteMatrix(newMatrix);
        return 1;
    }

    std::cout << "Original matrix:" << std::endl;
    printMatrix(matrix);
    deleteMatrix(matrix);

    std::cout << "Result matrix:" << std::endl;
    printMatrix(newMatrix);
    deleteMatrix(newMatrix);

    return 0;
}
