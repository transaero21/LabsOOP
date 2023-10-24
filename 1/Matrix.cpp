#include <iostream>
#include <limits>

#include "Matrix.h"
#include "Utils.h"

Matrix inputMatrix() {
    Matrix matrix;
    Value value;
    int n;

    try {
        std::cout << "Enter number of rows: ";
        matrix.m = getInt(1);
        std::cout << "Enter number of columns: ";
        matrix.n = getInt(1);

        for (int i = 0; i < matrix.m; i++) {
            std::cout << "Enter numbers for " << i + 1 << " row: ";
            for (int j = 0; j < matrix.n; j++) {
                n = getInt();
                if (n != 0) {
                    value = {.x = j, .y = i, .value = n};
                    appendArray<Value>(matrix.values, value);
                }
            }
        }
    } catch (...) {
        deleteMatrix(matrix);
        throw;
    }

    return matrix;
}

void printMatrix(const Matrix &matrix) {
    Value value;
    int i = 0;

    for (int y = 0; y < matrix.m; y++) {
        for (int x = 0; x < matrix.n; x++) {
            if (x > 0) std::cout << '\t';
            if (i != matrix.values.size) {
                value = matrix.values.data[i];
                if (value.x == x && value.y == y) {
                    std::cout << value.value;
                    i++;
                    continue;
                }
            }
            std::cout << '0';
        }
        std::cout << '\n';
    }
    std::cout << std::endl;
}

Matrix sortMatrix(const Matrix &matrix) {
    int min = std::numeric_limits<int>::max();
    int m = 0, l = 0;
    Array<Value> values;

    for (int i = 0; i < matrix.values.size; i++) {
        if (min > matrix.values.data[i].value) {
            min = matrix.values.data[i].value;
            m = i;
        }
        if (i == matrix.values.size - 1 || l < matrix.values.data[i + 1].y) {
            for (int j = m; j <= i; j++) {
                appendArray<Value>(values, matrix.values.data[j]);
            }
            if (i != matrix.values.size - 1) {
                min = std::numeric_limits<int>::max();
                l = matrix.values.data[i + 1].y;
                m = i;
            }
        }
    }

    Matrix ret = {.m = matrix.m, .n = matrix.n, .values = values};
    return ret;
}

void deleteMatrix(const Matrix &matrix) {
    delete[] matrix.values.data;
}