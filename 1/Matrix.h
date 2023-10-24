#ifndef MATRIX_H
#define MATRIX_H

#include "Utils.h"

struct Value {
    int x = 0;
    int y = 0;
    int value = 0;
};

struct Matrix {
    int m = 0;
    int n = 0;
    Array<Value> values;
};

Matrix inputMatrix();
void printMatrix(const Matrix& matrix);
Matrix sortMatrix(const Matrix &matrix);
void deleteMatrix(const Matrix &matrix);

#endif /* MATRIX_H */
